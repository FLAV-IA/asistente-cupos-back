package com.edu.asistente_cupos.observacion;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class ParalelizadorConMetrica {
  private final MeterRegistry meterRegistry;
  private final ExecutorService executor = Executors.newFixedThreadPool(4);

  public <T, R> List<R> procesar(String nombreMetrica, List<T> inputs, Function<T, R> funcion) {
    List<CompletableFuture<R>> tareas = inputs.stream()
                                              .map(input -> CompletableFuture.supplyAsync(() -> {
                                                try {
                                                  return meterRegistry.timer(nombreMetrica)
                                                                      .recordCallable(
                                                                        () -> funcion.apply(input));
                                                } catch (Exception e) {
                                                  throw new RuntimeException(e);
                                                }
                                              }, executor)).toList();

    return tareas.stream().map(CompletableFuture::join).toList();
  }
}

