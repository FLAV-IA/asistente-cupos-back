package com.edu.asistente_cupos.service.priorizacion;

import com.edu.asistente_cupos.domain.peticion.PeticionInscripcion;
import com.edu.asistente_cupos.domain.priorizacion.PeticionPorMateriaPriorizada;
import com.edu.asistente_cupos.service.priorizacion.dto.ResultadoPriorizacionLLM;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.edu.asistente_cupos.testutils.TestDataFactory.crearPeticionInscripcionDummy;
import static com.edu.asistente_cupos.testutils.TestDataFactory.crearResultadoPriorizacionLLMDummy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ConversorResultadoLLMTest {
  private final ConversorResultadoLLM conversor = new ConversorResultadoLLM();

  @Test
  void convierteResultadosLlmEnPeticionesPriorizadas() {
    PeticionInscripcion inscripcion = crearPeticionInscripcionDummy();
    ResultadoPriorizacionLLM resultado = crearResultadoPriorizacionLLMDummy();

    List<PeticionPorMateriaPriorizada> resultadoFinal = conversor.desdeResultadosLLM(
      List.of(resultado), List.of(inscripcion));

    assertThat(resultadoFinal).hasSize(1);
    PeticionPorMateriaPriorizada pri = resultadoFinal.get(0);
    assertThat(pri.getEstudiante().getDni()).isEqualTo(inscripcion.getEstudiante().getDni());
    assertThat(pri.getMateria()).isNotNull();
    assertThat(pri.getMotivo()).isNotBlank();
    assertThat(pri.getPrioridad()).isEqualTo(91);
  }

  @Test
  void lanzaExcepcionSiNoSeEncuentraInscripcionPorDni() {
    PeticionInscripcion inscripcion = crearPeticionInscripcionDummy();
    ResultadoPriorizacionLLM resultado = crearResultadoPriorizacionLLMDummy();
    resultado.setA("99999999");

    var ex = assertThrows(IllegalArgumentException.class,
      () -> conversor.desdeResultadosLLM(List.of(resultado), List.of(inscripcion)));

    assertThat(ex.getMessage()).contains("No se encontró la inscripción del estudiante");
  }

  @Test
  void lanzaExcepcionSiNoSeEncuentraMateriaEnInscripcion() {
    PeticionInscripcion inscripcion = crearPeticionInscripcionDummy();
    ResultadoPriorizacionLLM resultado = crearResultadoPriorizacionLLMDummy();
    resultado.getEp().get(0).setN("CODIGO_INEXISTENTE");

    var ex = assertThrows(IllegalArgumentException.class,
      () -> conversor.desdeResultadosLLM(List.of(resultado), List.of(inscripcion)));

    assertThat(ex.getMessage()).contains("No se encontró una petición original para la materia");
  }
}
