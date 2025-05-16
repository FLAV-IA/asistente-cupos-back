package com.edu.asistenteCupos.service.llm;

import com.edu.asistenteCupos.Utils.JsonConverter;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class RespuestaLLMParser {
  public <T> List<T> parsear(ChatResponse response, TypeReference<List<T>> typeRef) {
    String json = extraerJson(response);

    try {
      return JsonConverter.readList(json, typeRef);
    } catch (Exception e) {
      log.error("Error al parsear el JSON generado por el LLM. Contenido:\n{}", json);
      throw new RuntimeException(
        "No se pudo parsear la respuesta del modelo. Ver logs para más detalle.", e);
    }
  }

  private String extraerJson(ChatResponse response) {
    AssistantMessage assistantMessage = response.getResult().getOutput();
    String content = Objects.requireNonNull(assistantMessage.getText()).trim();
    log.info("Respuesta del modelo: {}", content);

    if (content.isBlank()) {
      log.warn("Respuesta del LLM vacía.");
      throw new RuntimeException("La respuesta del LLM está vacía.");
    }

    if (content.startsWith("```json") || content.startsWith("```")) {
      content = content.replaceAll("(?s)^```json\\s*", "").replaceAll("(?s)^```\\s*", "")
                       .replaceAll("(?s)```$", "").trim();
    }

    if (content.startsWith("[")) {
      return content;
    }

    int inicio = content.indexOf("[");
    int fin = content.lastIndexOf("]");

    if (inicio >= 0 && fin > inicio) {
      return content.substring(inicio, fin + 1).trim();
    }

    log.error("No se encontró un array JSON válido en la respuesta del modelo:\n{}", content);
    throw new RuntimeException("La respuesta del modelo no contiene un array JSON válido.");
  }
}
