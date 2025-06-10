package com.edu.asistente_cupos.service.llm;

import com.edu.asistente_cupos.excepcion.CuotaDeOpenAiInsuficienteException;
import com.edu.asistente_cupos.service.prompt.PromptTokenizerEstimator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.retry.NonTransientAiException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OpenAiLlmClient implements LlmClient {
  private final OpenAiChatModel chatModel;

  @Override
  public ChatResponse call(Prompt prompt) {
    log.info("Prompt con aprox. {} tokens.",
      PromptTokenizerEstimator.estimarTokens(prompt.getContents()));
    try {
      return chatModel.call(prompt);
    } catch (NonTransientAiException e) {
      log.error("Falló la llamada al modelo LLM", e);
      if (e.getMessage() != null && e.getMessage().contains("insufficient_quota")) {
        throw new CuotaDeOpenAiInsuficienteException(
          "No hay créditos disponibles para el token de OpenAI.", e);
      }
      throw e;
    }
  }
}
