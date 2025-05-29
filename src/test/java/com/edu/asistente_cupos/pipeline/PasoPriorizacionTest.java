package com.edu.asistente_cupos.pipeline;

import com.edu.asistente_cupos.domain.peticion.PeticionInscripcion;
import com.edu.asistente_cupos.domain.priorizacion.PeticionPorMateriaPriorizada;
import com.edu.asistente_cupos.service.priorizacion.ConversorResultadoLLM;
import com.edu.asistente_cupos.service.priorizacion.PriorizadorDePeticiones;
import com.edu.asistente_cupos.service.priorizacion.dto.ResultadoPriorizacionLLM;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.edu.asistente_cupos.testutils.TestDataFactory.crearPeticionInscripcionDummy;
import static com.edu.asistente_cupos.testutils.TestDataFactory.crearPeticionPriorizadaDummy;
import static com.edu.asistente_cupos.testutils.TestDataFactory.crearResultadoPriorizacionLLMDummy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PasoPriorizacionTest {
  @Test
  void AlEjecutarConPeticionesValidasRetornaLasPeticionesPriorizadas() {
    List<PeticionInscripcion> peticiones = List.of(crearPeticionInscripcionDummy(),
      crearPeticionInscripcionDummy());
    List<ResultadoPriorizacionLLM> resultadosLLM = List.of(crearResultadoPriorizacionLLMDummy());
    List<PeticionPorMateriaPriorizada> priorizadas = List.of(crearPeticionPriorizadaDummy());

    PriorizadorDePeticiones mockPriorizador = mock(PriorizadorDePeticiones.class);
    ConversorResultadoLLM mockConversor = mock(ConversorResultadoLLM.class);

    PasoPriorizacion paso = new PasoPriorizacion(mockPriorizador, mockConversor);
    when(mockPriorizador.priorizar(peticiones)).thenReturn(resultadosLLM);
    when(mockConversor.desdeResultadosLLM(resultadosLLM, peticiones)).thenReturn(priorizadas);


    List<PeticionPorMateriaPriorizada> resultado = paso.ejecutar(peticiones);


    assertThat(resultado).isEqualTo(priorizadas);
    verify(mockPriorizador).priorizar(peticiones);
    verify(mockConversor).desdeResultadosLLM(resultadosLLM, peticiones);
  }
}