package com.edu.asistente_cupos.service.prompt;

import com.edu.asistente_cupos.Utils.JsonConverter;

import java.util.function.ToIntFunction;

/**
 * Estima la cantidad de tokens para una string de texto.
 * Basado en que 1 token ≈ 0.75 palabras o 4 caracteres en inglés promedio.
 * Revisar tiktoken después.
 */
public class PromptTokenizerEstimator {
  public static int estimarTokens(String texto) {
    if (texto == null || texto.isBlank())
      return 0;
    return (int) Math.ceil(texto.length() / 4.0);
  }

  public static int estimarTokens(Object objeto) {
    String json = JsonConverter.toJson(objeto);
    return estimarTokens(json);
  }

  public static <T> ToIntFunction<T> estimadorDeObjeto() {
    return PromptTokenizerEstimator::estimarTokens;
  }
}
