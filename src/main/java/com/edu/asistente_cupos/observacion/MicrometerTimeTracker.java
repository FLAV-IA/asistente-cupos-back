package com.edu.asistente_cupos.observacion;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;

@Component
@RequiredArgsConstructor
public class MicrometerTimeTracker implements TimeTracker {
  private final MeterRegistry meterRegistry;

  @Override
  public <T> T track(String metricName, Callable<T> callable) {
    try {
      return meterRegistry.timer(metricName).recordCallable(callable);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void trackRunnable(String metricName, Runnable runnable) {
    meterRegistry.timer(metricName).record(runnable);
  }
}
