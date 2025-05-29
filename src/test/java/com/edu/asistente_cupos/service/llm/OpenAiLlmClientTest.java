package com.edu.asistente_cupos.service.llm;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OpenAiLlmClientTest {
  @Test
  void llamaAlModeloYDevuelveLaRespuesta() {
    OpenAiChatModel mockModel = mock(OpenAiChatModel.class);
    OpenAiLlmClient client = new OpenAiLlmClient(mockModel);

    Prompt prompt = new Prompt(List.of(new UserMessage("mensaje de prueba")));
    ChatResponse respuestaEsperada = new ChatResponse(List.of());

    when(mockModel.call(prompt)).thenReturn(respuestaEsperada);


    ChatResponse respuesta = client.call(prompt);


    assertThat(respuesta).isEqualTo(respuestaEsperada);
    verify(mockModel).call(prompt);
  }
}
