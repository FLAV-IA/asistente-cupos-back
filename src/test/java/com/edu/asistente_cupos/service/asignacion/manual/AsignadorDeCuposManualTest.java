package com.edu.asistente_cupos.service.asignacion.manual;

import com.edu.asistente_cupos.domain.Comision;
import com.edu.asistente_cupos.domain.Materia;
import com.edu.asistente_cupos.domain.priorizacion.PeticionPorMateriaPriorizada;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaAsignada;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaInscripcion;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaRechazada;
import com.edu.asistente_cupos.repository.ComisionRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static com.edu.asistente_cupos.testutils.TestDataFactory.crearEstudianteDummy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AsignadorDeCuposManualTest {
  @Test
  void asignaCupoSiHayDisponibilidad() {
    Materia materia = Materia.builder().codigo("MAT1").nombre("Mat").build();
    Comision comision = new Comision("COM1", "lunes", 1, materia);

    PeticionPorMateriaPriorizada peticion = new PeticionPorMateriaPriorizada(crearEstudianteDummy(),
      materia, List.of(comision), true, 99, "AVZ");

    ComisionRepository mockRepo = mock(ComisionRepository.class);
    when(mockRepo.findByCodigoIn(Set.of("COM1"))).thenReturn(List.of(comision));

    AsignadorDeCuposManual asignador = new AsignadorDeCuposManual(mockRepo);


    List<SugerenciaInscripcion> resultado = asignador.asignar(List.of(peticion));


    assertThat(resultado).hasSize(1);
    assertThat(resultado.get(0)).isInstanceOf(SugerenciaAsignada.class);
    assertThat(resultado.get(0).estudiante()).isEqualTo(peticion.getEstudiante());
  }

  @Test
  void rechazaPeticionSiNoHayCupos() {
    Materia materia = Materia.builder().codigo("MAT1").nombre("Mat").build();
    Comision comision = new Comision("COM1", "lunes", 0, materia);

    PeticionPorMateriaPriorizada peticion = new PeticionPorMateriaPriorizada(crearEstudianteDummy(),
      materia, List.of(comision), true, 99, "COR");

    ComisionRepository mockRepo = mock(ComisionRepository.class);
    when(mockRepo.findByCodigoIn(Set.of("COM1"))).thenReturn(List.of(comision));

    AsignadorDeCuposManual asignador = new AsignadorDeCuposManual(mockRepo);


    List<SugerenciaInscripcion> resultado = asignador.asignar(List.of(peticion));


    assertThat(resultado).hasSize(1);
    assertThat(resultado.get(0)).isInstanceOf(SugerenciaRechazada.class);
  }

  @Test
  void soloAsignaCupoAQuienTieneMayorPrioridad() {
    Materia materia = Materia.builder().codigo("MAT1").nombre("Mat").build();
    Comision comision = new Comision("COM1", "lunes", 1, materia);

    PeticionPorMateriaPriorizada alta = new PeticionPorMateriaPriorizada(
      crearEstudianteDummy("111"), materia, List.of(comision), true, 100, "AVZ");
    PeticionPorMateriaPriorizada baja = new PeticionPorMateriaPriorizada(
      crearEstudianteDummy("222"), materia, List.of(comision), true, 10, "REC");

    ComisionRepository mockRepo = mock(ComisionRepository.class);
    when(mockRepo.findByCodigoIn(Set.of("COM1"))).thenReturn(List.of(comision));

    AsignadorDeCuposManual asignador = new AsignadorDeCuposManual(mockRepo);


    List<SugerenciaInscripcion> resultado = asignador.asignar(List.of(baja, alta));


    assertThat(resultado).hasSize(2);
    assertThat(resultado.get(0)).isInstanceOf(SugerenciaAsignada.class);
    assertThat(resultado.get(0).estudiante().getDni()).isEqualTo("111");
    assertThat(resultado.get(1)).isInstanceOf(SugerenciaRechazada.class);
  }
}
