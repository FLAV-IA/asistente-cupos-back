package com.edu.asistenteCupos.service.llm;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RespuestaLLMParserTest {
  private final RespuestaLLMParser parser = new RespuestaLLMParser();

  @Test
  void parseaJsonValidoDirecto() {
    String json = "[{\"a\": \"123\", \"p\": 90}]";
    ChatResponse response = crearRespuestaConTexto(json);

    List<TestDto> resultado = parser.parsear(response, new TypeReference<>() {});

    assertThat(resultado).hasSize(1);
    assertThat(resultado.get(0).a).isEqualTo("123");
    assertThat(resultado.get(0).p).isEqualTo(90);
  }

  @Test
  void parseaJsonDentroDeBloqueDeCodigo() {
    String json = "```json\n[{\"a\": \"123\", \"p\": 90}]\n```";
    ChatResponse response = crearRespuestaConTexto(json);

    List<TestDto> resultado = parser.parsear(response, new TypeReference<>() {});

    assertThat(resultado).hasSize(1);
  }

  @Test
  void lanzaExcepcionSiContenidoEstaVacio() {
    ChatResponse response = crearRespuestaConTexto("   ");

    RuntimeException ex = assertThrows(RuntimeException.class,
      () -> parser.parsear(response, new TypeReference<List<TestDto>>() {}));

    assertThat(ex.getMessage()).contains("vacía");
  }

  @Test
  void lanzaExcepcionSiNoTieneArrayJsonValido() {
    String mal = "no hay ningún array";
    ChatResponse response = crearRespuestaConTexto(mal);

    RuntimeException ex = assertThrows(RuntimeException.class,
      () -> parser.parsear(response, new TypeReference<List<TestDto>>() {}));

    assertThat(ex.getMessage()).contains("array JSON válido");
  }

  private ChatResponse crearRespuestaConTexto(String texto) {
    AssistantMessage message = new AssistantMessage(texto);
    Generation generation = new Generation(message);
    return new ChatResponse(List.of(generation));
  }

  public static class TestDto {
    public String a;
    public int p;
  }
}
