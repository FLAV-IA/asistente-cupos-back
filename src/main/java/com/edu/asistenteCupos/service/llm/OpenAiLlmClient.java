package com.edu.asistenteCupos.service.llm;

import com.edu.asistenteCupos.service.prompt.PromptTokenizerEstimator;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OpenAiLlmClient implements LlmClient {
  private final OpenAiChatModel chatModel;

  @Override
  public ChatResponse call(Prompt prompt) {
    System.out.println(
      "Prompt con aprox. " + PromptTokenizerEstimator.estimarTokens(prompt.getContents()) +
        " tokens.");
    return chatModel.call(prompt);
  }
}