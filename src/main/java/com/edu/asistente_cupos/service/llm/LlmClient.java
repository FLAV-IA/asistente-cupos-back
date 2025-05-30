package com.edu.asistente_cupos.service.llm;

import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;

public interface LlmClient {
  ChatResponse call(Prompt prompt);
}
