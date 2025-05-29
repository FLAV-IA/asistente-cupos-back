package com.edu.asistente_cupos.domain.prompt;

import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.DefaultChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.Content;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PromptPrinter {
  public static String imprimir(Prompt prompt, boolean agrupado) {
    return agrupado ? aTextoAgrupado(prompt) : aTextoCompleto(prompt);
  }

  private static String aTextoAgrupado(Prompt prompt) {
    StringBuilder builder = new StringBuilder();

    Map<MessageType, List<Content>> mensajesPorTipo = prompt.getInstructions().stream().collect(
      Collectors.groupingBy(content -> {
        Object tipo = content.getMetadata().get("messageType");
        if (tipo instanceof MessageType mt)
          return mt;
        if (tipo instanceof String s)
          return MessageType.valueOf(s);
        return MessageType.USER;
      }));

    for (MessageType tipo : MessageType.values()) {
      List<Content> mensajes = mensajesPorTipo.get(tipo);
      if (mensajes != null && !mensajes.isEmpty()) {
        builder.append("### ").append(tipo.name()).append(" MESSAGES ###\n\n");
        for (Content mensaje : mensajes) {
          builder.append(mensaje.getText()).append("\n\n");
        }
      }
    }

    appendOpciones(prompt, builder);
    return builder.toString();
  }

  private static String aTextoCompleto(Prompt prompt) {
    StringBuilder builder = new StringBuilder();

    for (Content content : prompt.getInstructions()) {
      Object tipo = content.getMetadata().get("messageType");
      String tipoTexto = (tipo instanceof MessageType mt) ? mt.name() : String.valueOf(tipo);
      builder.append("[").append(tipoTexto).append("] ").append(content.getText()).append("\n\n");
    }

    appendOpciones(prompt, builder);
    return builder.toString();
  }

  private static void appendOpciones(Prompt prompt, StringBuilder builder) {
    ChatOptions options = prompt.getOptions();
    if (options instanceof DefaultChatOptions defaultOptions) {
      builder.append("--- OPCIONES DEL MODELO ---\n");
      builder.append("Temperature: ").append(defaultOptions.getTemperature()).append("\n");
      builder.append("TopP: ").append(defaultOptions.getTopP()).append("\n");
      builder.append("MaxTokens: ").append(defaultOptions.getMaxTokens()).append("\n");
      builder.append("StopSequences: ").append(defaultOptions.getStopSequences()).append("\n");
    }
  }
}
