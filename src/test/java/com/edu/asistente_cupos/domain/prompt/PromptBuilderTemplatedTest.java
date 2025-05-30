package com.edu.asistente_cupos.domain.prompt;

import com.edu.asistente_cupos.Utils.prompt.PromptTemplateProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.core.io.ByteArrayResource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PromptBuilderTemplatedTest {
  private PromptTemplateProvider templateProvider;

  @BeforeEach
  void setUp() {
    templateProvider = mock(PromptTemplateProvider.class);
  }

  @Test
  void lanzaExcepcionSiNoSePasanVariables() {
    PromptBuilderTemplated builder = PromptBuilderTemplated.nuevo(templateProvider)
                                                           .conSystemTemplate("system.txt")
                                                           .conUserTemplate("user.txt");

    assertThatThrownBy(builder::construir).isInstanceOf(IllegalArgumentException.class)
                                          .hasMessageContaining(
                                            "Tenés que pasar al menos una variable.");
  }

  @Test
  void lanzaExcepcionSiNoSeDefineSystemTemplate() {
    PromptBuilderTemplated builder = PromptBuilderTemplated.nuevo(templateProvider)
                                                           .conUserTemplate("user.txt")
                                                           .conVariable("x", "valor");

    assertThatThrownBy(builder::construir).isInstanceOf(IllegalArgumentException.class)
                                          .hasMessageContaining("Content must not be null");
  }

  @Test
  void lanzaExcepcionSiElTemplateSystemEsNull() {
    when(templateProvider.getTemplate("user.txt")).thenReturn(
      new ByteArrayResource("Hola ${x}".getBytes()));
    when(templateProvider.leerTemplateComoString("system.txt")).thenReturn(null);

    PromptBuilderTemplated builder = PromptBuilderTemplated.nuevo(templateProvider)
                                                           .conUserTemplate("user.txt")
                                                           .conSystemTemplate("system.txt")
                                                           .conVariable("mundo", "mundo");

    assertThatThrownBy(builder::construir).isInstanceOf(IllegalArgumentException.class)
                                          .hasMessageContaining("Content must not be null");
  }

  @Test
  void generaPromptCorrectamenteConTemplatesValidos() {
    when(templateProvider.getTemplate("prompt/user.txt")).thenReturn(
      new ByteArrayResource("Hola {x}".getBytes()));

    when(templateProvider.leerTemplateComoString("prompt/system.txt")).thenReturn(
      "Instrucción para el modelo");

    Prompt prompt = PromptBuilderTemplated.nuevo(templateProvider)
                                          .conSystemTemplate("prompt/system.txt")
                                          .conUserTemplate("prompt/user.txt")
                                          .conVariable("x", "mundo").construir();

    assertThat(prompt.getInstructions()).hasSize(2);

    assertThat(prompt.getInstructions()).extracting(Object::toString)
                                        .anyMatch(text -> text.contains("Hola mundo"));

    assertThat(prompt.getInstructions()).extracting(Object::toString).anyMatch(
      text -> text.contains("Instrucción para el modelo"));
  }
}
