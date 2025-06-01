package com.edu.asistente_cupos.observacion;

import java.util.concurrent.Callable;

public interface TimeTracker {
  <T> T track(String metricName, Callable<T> callable);

  void trackRunnable(String metricName, Runnable runnable);
}

