package com.edu.asistenteCupos.service.prompt;

import org.springframework.ai.chat.prompt.Prompt;

public interface PromptGenerator<T> {
  Prompt crearPrompt(T entrada);
}