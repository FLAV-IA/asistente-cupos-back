package com.edu.asistente_cupos.observacion;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MicrometerTimeTrackerTest {
  @Test
  void trackEjecutaCallableYDevuelveResultado() throws Exception {
    MeterRegistry registry = mock(MeterRegistry.class);
    Timer timer = mock(Timer.class);
    when(registry.timer("test.metric")).thenReturn(timer);
    when(timer.recordCallable(any())).thenReturn("resultado");

    MicrometerTimeTracker tracker = new MicrometerTimeTracker(registry);

    String resultado = tracker.track("test.metric", () -> "resultado");

    assertThat(resultado).isEqualTo("resultado");
    verify(timer).recordCallable(any());
  }

  @Test
  void trackCapturaYEnvuelveExcepcionEnRuntimeException() throws Exception {
    MeterRegistry registry = mock(MeterRegistry.class);
    Timer timer = mock(Timer.class);
    when(registry.timer("test.metric")).thenReturn(timer);
    when(timer.recordCallable(any())).thenThrow(new RuntimeException("fallo"));

    MicrometerTimeTracker tracker = new MicrometerTimeTracker(registry);

    assertThrows(RuntimeException.class, () -> {
      tracker.track("test.metric", () -> {
        throw new Exception("error");
      });
    });
  }
}
