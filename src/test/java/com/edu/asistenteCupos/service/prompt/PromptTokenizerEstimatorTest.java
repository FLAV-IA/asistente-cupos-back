package com.edu.asistenteCupos.service.prompt;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntFunction;

import static org.assertj.core.api.Assertions.assertThat;

class PromptTokenizerEstimatorTest {
  @Test
  void estimarTokensDevuelveCeroParaTextoVacioONulo() {
    assertThat(PromptTokenizerEstimator.estimarTokens("")).isEqualTo(0);
    assertThat(PromptTokenizerEstimator.estimarTokens("   ")).isEqualTo(0);
    assertThat(PromptTokenizerEstimator.estimarTokens(null)).isEqualTo(0);
  }

  @Test
  void estimarTokensDevuelveTokensAproximadosPorTexto() {
    String texto = "Este es un texto de prueba para estimar tokens";
    int esperado = (int) Math.ceil(texto.length() / 4.0);

    int tokens = PromptTokenizerEstimator.estimarTokens(texto);

    assertThat(tokens).isEqualTo(esperado);
  }

  @Test
  void estimarTokensDevuelveTokensAproximadosPorObjeto() {
    DummyObjeto dummy = new DummyObjeto("valor", 123);
    String json = com.edu.asistenteCupos.Utils.JsonConverter.toJson(dummy);
    int esperado = (int) Math.ceil(json.length() / 4.0);

    int tokens = PromptTokenizerEstimator.estimarTokens(dummy);

    assertThat(tokens).isEqualTo(esperado);
  }

  @Test
  void estimadorDeObjetoFuncionaCorrectamente() {
    DummyObjeto dummy = new DummyObjeto("hola", 42);
    ToIntFunction<DummyObjeto> estimador = PromptTokenizerEstimator.estimadorDeObjeto();

    int tokens = estimador.applyAsInt(dummy);

    assertThat(tokens).isEqualTo(PromptTokenizerEstimator.estimarTokens(dummy));
  }

  private record DummyObjeto(String campo1, int campo2) {}
}
