package com.edu.asistente_cupos.observacion;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.Callable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ParalelizadorConMetricaTest {
  @Test
  void procesarEjecutaFuncionSobreTodosLosElementos() throws Exception {
    MeterRegistry registry = mock(MeterRegistry.class);
    Timer timer = mock(Timer.class);
    when(registry.timer("metric.batch")).thenReturn(timer);

    when(timer.recordCallable(any())).thenAnswer(invocation -> {
      Callable<?> callable = invocation.getArgument(0);
      return callable.call();
    });


    ParalelizadorConMetrica paralelizador = new ParalelizadorConMetrica(registry);


    List<String> inputs = List.of("a", "b", "c");
    List<String> resultado = paralelizador.procesar("metric.batch", inputs, s -> s + "_ok");

    assertThat(resultado).containsExactly("a_ok", "b_ok", "c_ok");
    verify(registry, times(3)).timer("metric.batch");
  }
}