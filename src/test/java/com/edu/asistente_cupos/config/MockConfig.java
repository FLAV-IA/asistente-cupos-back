package com.edu.asistente_cupos.config;

import com.edu.asistente_cupos.service.llm.LlmClient;
import com.edu.asistente_cupos.testutils.TestDataFactory;
import org.mockito.ArgumentMatcher;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Configuration
public class MockConfig {
  @Bean
  public LlmClient llmClient() {
    LlmClient llmClient = mock(LlmClient.class);

    ArgumentMatcher<Prompt> esPromptDePriorizacion = prompt -> prompt != null &&
      prompt.getInstructions() != null && prompt.getInstructions().stream().anyMatch(
      msg -> msg.getText().contains("evaluate their course enrollment requests"));

    ArgumentMatcher<Prompt> esPromptDeTraduccion = prompt -> prompt != null &&
      prompt.getInstructions() != null &&
      prompt.getInstructions().stream().anyMatch(msg -> msg.getText().contains("cupoAsignado"));

    when(llmClient.call(argThat(esPromptDePriorizacion))).thenReturn(
      TestDataFactory.respuestaChatResponsePriorizacion());

    when(llmClient.call(argThat(esPromptDeTraduccion))).thenReturn(
      TestDataFactory.respuestaChatResponseTraduccion());

    return llmClient;
  }
}