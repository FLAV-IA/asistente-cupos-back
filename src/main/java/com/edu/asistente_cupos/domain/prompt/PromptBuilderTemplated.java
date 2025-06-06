package com.edu.asistente_cupos.domain.prompt;

import com.edu.asistente_cupos.Utils.prompt.PromptTemplateProvider;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PromptBuilderTemplated {
  private final PromptTemplateProvider promptTemplateProvider;
  private final Map<String, Object> variables = new HashMap<>();
  private String systemTemplatePath;
  private String userTemplatePath;

  private PromptBuilderTemplated(PromptTemplateProvider promptTemplateProvider) {
    this.promptTemplateProvider = promptTemplateProvider;
  }

  public static PromptBuilderTemplated nuevo(PromptTemplateProvider provider) {
    if (provider == null)
      throw new IllegalArgumentException("PromptTemplateProvider no puede ser nulo.");
    return new PromptBuilderTemplated(provider);
  }

  public PromptBuilderTemplated conVariable(String clave, Object valor) {
    variables.put(clave, valor);
    return this;
  }

  public Prompt construir() {
    if (variables.isEmpty()) {
      throw new IllegalArgumentException("Tenés que pasar al menos una variable.");
    }
    return new Prompt(List.of(systemMessageDesde(), userMessageDesde()));
  }

  private Message userMessageDesde() {
    if (userTemplatePath == null)
      throw new IllegalStateException("Falta el path del template user");
    PromptTemplate userPrompt = new PromptTemplate(
      promptTemplateProvider.getTemplate(userTemplatePath));
    return new UserMessage(userPrompt.create(variables).getContents());
  }

  private Message systemMessageDesde() {
    if (systemTemplatePath == null)
      throw new IllegalStateException("Falta el path del template system");
    return new SystemMessage(promptTemplateProvider.leerTemplateComoString(systemTemplatePath));
  }

  public PromptBuilderTemplated conSystemTemplate(String path) {
    this.systemTemplatePath = path;
    return this;
  }

  public PromptBuilderTemplated conUserTemplate(String path) {
    this.userTemplatePath = path;
    return this;
  }
}
