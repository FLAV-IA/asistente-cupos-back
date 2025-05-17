package com.edu.asistenteCupos.testutils;

import java.util.List;
import java.util.function.ToIntFunction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class BatcherTestUtils {
  public static <T> void assertBatchesDentroDelLimite(List<List<T>> batches, ToIntFunction<T> estimador, int maxTokens) {
    assertAll("tokens por batch", batches.stream().map(
      batch -> () -> assertThat(batch.stream().mapToInt(estimador).sum())
        .as("Tokens en el batch no deben superar el límite").isLessThanOrEqualTo(maxTokens)));
  }
}
