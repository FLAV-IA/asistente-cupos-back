package com.edu.asistenteCupos.pipeline;

@FunctionalInterface
public interface Paso<I, O> {
  O ejecutar(I input);
}
