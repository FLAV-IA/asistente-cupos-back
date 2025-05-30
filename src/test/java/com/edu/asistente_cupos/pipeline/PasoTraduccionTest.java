package com.edu.asistente_cupos.pipeline;

import com.edu.asistente_cupos.domain.sugerencia.SugerenciaInscripcion;
import com.edu.asistente_cupos.observacion.ParalelizadorConMetrica;
import com.edu.asistente_cupos.observacion.TimeTracker;
import com.edu.asistente_cupos.service.traduccion.ConversorSugerenciasLLM;
import com.edu.asistente_cupos.service.traduccion.TraductorDeSugerencias;
import com.edu.asistente_cupos.service.traduccion.dto.SugerenciaInscripcionLLM;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.Callable;

import static com.edu.asistente_cupos.testutils.TestDataFactory.crearSugerenciaAsignadaDummy;
import static com.edu.asistente_cupos.testutils.TestDataFactory.crearSugerenciaLLMDummy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PasoTraduccionTest {
  @Test
  void alEjecutarLaTraduccionDeLasSugerenciasRetornaLasSugerenciasTraducidasCorrectamente() throws Exception {
    TraductorDeSugerencias traductor = mock(TraductorDeSugerencias.class);
    ConversorSugerenciasLLM conversor = mock(ConversorSugerenciasLLM.class);
    TimeTracker tracker = mock(TimeTracker.class);
    ParalelizadorConMetrica paralelizador = mock(ParalelizadorConMetrica.class);

    PasoTraduccion paso = new PasoTraduccion(traductor, conversor, tracker, paralelizador);

    List<SugerenciaInscripcion> input = List.of(crearSugerenciaAsignadaDummy());
    List<List<SugerenciaInscripcion>> batches = List.of(input);
    List<SugerenciaInscripcionLLM> traducidas = List.of(crearSugerenciaLLMDummy());
    List<SugerenciaInscripcion> resultadoEsperado = List.of(crearSugerenciaAsignadaDummy());

    when(paralelizador.procesar(anyString(), eq(batches), any())).thenReturn(List.of(traducidas));

    when(conversor.desdeLLM(traducidas)).thenReturn(resultadoEsperado);

    when(tracker.track(anyString(), any(Callable.class))).thenAnswer(invocation -> {
      Callable<?> callable = invocation.getArgument(1);
      return callable.call();
    });


    List<SugerenciaInscripcion> resultado = paso.ejecutar(input);


    assertThat(resultado).isEqualTo(resultadoEsperado);
    verify(paralelizador).procesar(anyString(), eq(batches), any());
    verify(conversor).desdeLLM(traducidas);
    verify(tracker).track(eq("pipeline.traduccion.total"), any());
  }
}