package com.edu.asistente_cupos.Utils;

/**
 * Agrego esta interfaz que tiene 3 argumentos de entrada y 1 de salida para que los métodos del
 * ensamblador de peticiones que conceptualmente son similares, puedan utilizar uno genérico.
 *
 * @param <T>
 * @param <U>
 * @param <V>
 * @param <R>
 */
@FunctionalInterface
public interface TriFunction<T, U, V, R> {
  R apply(T t, U u, V v);
}
