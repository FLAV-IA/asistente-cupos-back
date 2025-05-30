package com.edu.asistente_cupos.service.llm;

import com.edu.asistente_cupos.service.prompt.PromptTokenizerEstimator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
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
    return chatModel.call(prompt);
  }
}