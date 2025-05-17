package com.edu.asistenteCupos.utils.llm;

import com.edu.asistenteCupos.Utils.llm.BatcherPorTokens;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.ToIntFunction;

import static com.edu.asistenteCupos.testutils.BatcherTestUtils.assertBatchesDentroDelLimite;
import static org.assertj.core.api.Assertions.assertThat;

class BatcherPorTokensTest {
  @Test
  void agrupaElementosEnUnSoloBatchSiNoSeSuperaElLimite() {
    List<String> elementos = List.of("uno", "dos", "tres");
    int maxTokens = 100;
    ToIntFunction<String> estimador = s -> 10;

    var resultado = BatcherPorTokens.dividir(elementos, maxTokens, estimador);

    assertThat(resultado).hasSize(1);
    assertThat(resultado.get(0)).containsExactlyElementsOf(elementos);
    assertBatchesDentroDelLimite(resultado, estimador, maxTokens);
  }

  @Test
  void divideElementosEnVariosBatchesCuandoSeSuperaElLimite() {
    List<String> elementos = List.of("a", "b", "c", "d", "e");
    int maxTokens = 30;
    ToIntFunction<String> estimador = s -> 12;

    var resultado = BatcherPorTokens.dividir(elementos, maxTokens, estimador);

    assertThat(resultado).hasSize(3);
    assertThat(resultado.get(0)).containsExactly("a", "b");
    assertThat(resultado.get(1)).containsExactly("c", "d");
    assertThat(resultado.get(2)).containsExactly("e");
    assertBatchesDentroDelLimite(resultado, estimador, maxTokens);
  }

  @Test
  void noRompeConListaVacia() {
    List<String> elementos = List.of();
    int maxTokens = 50;
    ToIntFunction<String> estimador = s -> 10;

    var resultado = BatcherPorTokens.dividir(elementos, maxTokens, estimador);

    assertThat(resultado).isEmpty();
  }

  @Test
  void manejaElementosConTokensVariables() {
    List<String> elementos = List.of("a", "bb", "ccc", "dddd", "eeeee");
    int maxTokens = 10;
    ToIntFunction<String> estimador = String::length;

    var resultado = BatcherPorTokens.dividir(elementos, maxTokens, estimador);

    assertThat(resultado).hasSizeGreaterThan(1);
    assertBatchesDentroDelLimite(resultado, estimador, maxTokens);
  }

  @Test
  void creaBatchUnicoParaElementoGrandeSiBatchEstaVacio() {
    List<String> elementos = List.of("elemento_grande");
    int maxTokens = 5;
    ToIntFunction<String> estimador = s -> 20;

    var resultado = BatcherPorTokens.dividir(elementos, maxTokens, estimador);

    assertThat(resultado).hasSize(1);
    assertThat(resultado.get(0)).containsExactly("elemento_grande");
  }
}