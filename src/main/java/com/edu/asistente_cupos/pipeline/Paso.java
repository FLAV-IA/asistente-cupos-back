package com.edu.asistente_cupos.pipeline;

@FunctionalInterface
public interface Paso<I, O> {
  O ejecutar(I input);
}
