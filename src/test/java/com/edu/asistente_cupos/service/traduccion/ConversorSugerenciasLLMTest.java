package com.edu.asistente_cupos.service.traduccion;

import com.edu.asistente_cupos.domain.Comision;
import com.edu.asistente_cupos.domain.Estudiante;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaAceptada;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaInscripcion;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaRechazada;
import com.edu.asistente_cupos.repository.ComisionRepository;
import com.edu.asistente_cupos.repository.EstudianteRepository;
import com.edu.asistente_cupos.service.traduccion.dto.SugerenciaInscripcionLLM;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.edu.asistente_cupos.testutils.TestDataFactory.crearComisionDummy;
import static com.edu.asistente_cupos.testutils.TestDataFactory.crearEstudianteDummy;
import static com.edu.asistente_cupos.testutils.TestDataFactory.crearSugerenciaLLMDummy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ConversorSugerenciasLLMTest {
  EstudianteRepository repoEstudiantes = mock(EstudianteRepository.class);
  ComisionRepository repoComisiones = mock(ComisionRepository.class);
  ConversorSugerenciasLLM conversor = new ConversorSugerenciasLLM(repoEstudiantes, repoComisiones);

  @Test
  void convierteAsignadaCorrectamente() {
    SugerenciaInscripcionLLM dto = crearSugerenciaLLMDummy();
    Estudiante estudiante = crearEstudianteDummy();
    Comision comision = crearComisionDummy();

    when(repoEstudiantes.findByDni(dto.getA())).thenReturn(Optional.of(estudiante));
    when(repoComisiones.findById(dto.getC())).thenReturn(Optional.of(comision));


    SugerenciaInscripcion resultado = conversor.convertir(dto);


    assertThat(resultado).isInstanceOf(SugerenciaAceptada.class);
    assertThat(resultado.estudiante()).isEqualTo(estudiante);
    assertThat(resultado.materia()).isEqualTo(comision.getMateria());
    assertThat(resultado.prioridad()).isEqualTo(dto.getP());
    assertThat(resultado.motivo()).isEqualTo(dto.getM());
  }

  @Test
  void convierteRechazadaCorrectamente() {
    SugerenciaInscripcionLLM dto = crearSugerenciaLLMDummy();
    dto.setX(false);
    Estudiante estudiante = crearEstudianteDummy();
    Comision comision = crearComisionDummy();

    when(repoEstudiantes.findByDni(dto.getA())).thenReturn(Optional.of(estudiante));
    when(repoComisiones.findById(dto.getC())).thenReturn(Optional.of(comision));


    SugerenciaInscripcion resultado = conversor.convertir(dto);


    assertThat(resultado).isInstanceOf(SugerenciaRechazada.class);
    assertThat(resultado.estudiante()).isEqualTo(estudiante);
    assertThat(resultado.materia()).isEqualTo(comision.getMateria());
    assertThat(resultado.prioridad()).isEqualTo(dto.getP());
    assertThat(resultado.motivo()).isEqualTo(dto.getM());
  }

  @Test
  void lanzaExcepcionSiNoEncuentraEstudiante() {
    SugerenciaInscripcionLLM dto = crearSugerenciaLLMDummy();

    when(repoEstudiantes.findByDni(dto.getA())).thenReturn(Optional.empty());

    RuntimeException ex = assertThrows(RuntimeException.class, () -> conversor.convertir(dto));
    assertThat(ex.getMessage()).contains("Estudiante no encontrado");
  }

  @Test
  void lanzaExcepcionSiNoEncuentraComision() {
    SugerenciaInscripcionLLM dto = crearSugerenciaLLMDummy();
    Estudiante estudiante = crearEstudianteDummy();

    when(repoEstudiantes.findByDni(dto.getA())).thenReturn(Optional.of(estudiante));
    when(repoComisiones.findById(dto.getC())).thenReturn(Optional.empty());

    RuntimeException ex = assertThrows(RuntimeException.class, () -> conversor.convertir(dto));
    assertThat(ex.getMessage()).contains("Comisión no encontrada");
  }

  @Test
  void convierteListaCompletaConDesdeLLM() {
    SugerenciaInscripcionLLM dto = crearSugerenciaLLMDummy();
    Estudiante estudiante = crearEstudianteDummy();
    Comision comision = crearComisionDummy();

    when(repoEstudiantes.findByDni(dto.getA())).thenReturn(Optional.of(estudiante));
    when(repoComisiones.findById(dto.getC())).thenReturn(Optional.of(comision));


    List<SugerenciaInscripcion> resultado = conversor.desdeLLM(List.of(dto, dto));


    assertThat(resultado).hasSize(2);
    assertThat(resultado.get(0)).isInstanceOf(SugerenciaAceptada.class);
  }
}
