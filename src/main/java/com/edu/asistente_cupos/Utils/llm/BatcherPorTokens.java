package com.edu.asistente_cupos.Utils.llm;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ToIntFunction;

public class BatcherPorTokens {
  public static <T> List<List<T>> dividir(List<T> elementos, int maxTokens, ToIntFunction<T> estimadorTokens) {
    List<List<T>> batches = new ArrayList<>();
    List<T> batchActual = new ArrayList<>();
    int tokensEnBatch = 0;

    for (T elemento : elementos) {
      int tokens = estimadorTokens.applyAsInt(elemento);
      if (tokensEnBatch + tokens > maxTokens && !batchActual.isEmpty()) {
        batches.add(new ArrayList<>(batchActual));
        batchActual.clear();
        tokensEnBatch = 0;
      }
      batchActual.add(elemento);
      tokensEnBatch += tokens;
    }

    if (!batchActual.isEmpty()) {
      batches.add(batchActual);
    }

    return batches;
  }
}
