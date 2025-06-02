package com.edu.asistente_cupos.pipeline;

import com.edu.asistente_cupos.domain.peticion.PeticionInscripcion;
import com.edu.asistente_cupos.domain.priorizacion.PeticionPorMateriaPriorizada;
import com.edu.asistente_cupos.observacion.ParalelizadorConMetrica;
import com.edu.asistente_cupos.observacion.TimeTracker;
import com.edu.asistente_cupos.service.priorizacion.ConversorResultadoLLM;
import com.edu.asistente_cupos.service.priorizacion.PriorizadorDePeticiones;
import com.edu.asistente_cupos.service.priorizacion.dto.ResultadoPriorizacionLLM;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.Callable;

import static com.edu.asistente_cupos.testutils.TestDataFactory.crearPeticionInscripcionDummy;
import static com.edu.asistente_cupos.testutils.TestDataFactory.crearPeticionPriorizadaDummy;
import static com.edu.asistente_cupos.testutils.TestDataFactory.crearResultadoPriorizacionLLMDummy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PasoPriorizacionTest {
  @Test
  void alEjecutarConPeticionesValidasRetornaLasPeticionesPriorizadas() {
    List<PeticionInscripcion> peticiones = List.of(crearPeticionInscripcionDummy(),
      crearPeticionInscripcionDummy());
    List<List<PeticionInscripcion>> batches = List.of(peticiones);
    List<ResultadoPriorizacionLLM> resultadosLLM = List.of(crearResultadoPriorizacionLLMDummy());
    List<PeticionPorMateriaPriorizada> priorizadas = List.of(crearPeticionPriorizadaDummy());

    PriorizadorDePeticiones mockPriorizador = mock(PriorizadorDePeticiones.class);
    ConversorResultadoLLM mockConversor = mock(ConversorResultadoLLM.class);
    ParalelizadorConMetrica mockParalelizador = mock(ParalelizadorConMetrica.class);
    TimeTracker mockTracker = mock(TimeTracker.class);

    when(mockTracker.track(anyString(), any())).thenAnswer(invocation -> {
      @SuppressWarnings(
        "unchecked") Callable<Object> callable = invocation.getArgument(1);
      return callable.call();
    });

    when(mockParalelizador.procesar(anyString(), eq(batches), any())).thenReturn(
      List.of(resultadosLLM));

    when(mockConversor.desdeResultadosLLM(resultadosLLM, peticiones)).thenReturn(priorizadas);


    PasoPriorizacion paso = new PasoPriorizacion(mockPriorizador, mockConversor, mockTracker,
      mockParalelizador);

    List<PeticionPorMateriaPriorizada> resultado = paso.ejecutar(peticiones);


    assertThat(resultado).isEqualTo(priorizadas);
    verify(mockConversor).desdeResultadosLLM(resultadosLLM, peticiones);
    verify(mockParalelizador).procesar(anyString(), eq(batches), any());
  }
}
