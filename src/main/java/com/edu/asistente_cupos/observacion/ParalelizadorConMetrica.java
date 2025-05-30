package com.edu.asistente_cupos.observacion;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.*;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class ParalelizadorConMetrica {
  private final MeterRegistry meterRegistry;
  private final ExecutorService executor = Executors.newFixedThreadPool(4);

  public <T, R> List<R> procesar(String nombreMetrica, List<T> inputs, Function<T, R> funcion) {
    List<CompletableFuture<R>> tareas = inputs.stream()
      .map(input -> CompletableFuture.supplyAsync(() ->
        meterRegistry.timer(nombreMetrica).record(() -> funcion.apply(input)), executor))
      .toList();

    return tareas.stream().map(CompletableFuture::join).toList();
  }
}
