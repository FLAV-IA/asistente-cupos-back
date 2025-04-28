package com.edu.asistenteCupos.service;

import com.edu.asistenteCupos.Utils.JsonConverter;
import com.edu.asistenteCupos.domain.PeticionInscripcion;
import com.edu.asistenteCupos.domain.SugerenciaInscripcion;
import com.edu.asistenteCupos.domain.prompt.PromptPrinter;
import com.edu.asistenteCupos.mapper.SugerenciaInscripcionMapper;
import com.edu.asistenteCupos.service.factory.PromptFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AsistenteDeInscripcion {
  private final OpenAiChatModel chatModel;
  private final PromptFactory promptFactory;
  private SugerenciaInscripcionMapper mapper;

  public List<SugerenciaInscripcion> sugerirInscripcion(List<PeticionInscripcion> peticionesDeInscripcion) {
    Prompt prompt = promptFactory.crearPrompt(peticionesDeInscripcion);
    ChatResponse respuesta = chatModel.call(prompt);
    return construirSugerenciasDesde(respuesta);
  }

  public String mostrarPrompt(List<PeticionInscripcion> peticionesDeInscripcion) {
    Prompt prompt = promptFactory.crearPrompt(peticionesDeInscripcion);
    System.out.println("el prompt es: \n" + PromptPrinter.imprimir(prompt, true));
    return PromptPrinter.imprimir(prompt, false);
  }

  private List<SugerenciaInscripcion> construirSugerenciasDesde(ChatResponse respuesta) {
    String json = extraerJson(respuesta.getResult().getOutput().toString());
    return parsearASugerencias(json);
  }

  private List<SugerenciaInscripcion> parsearASugerencias(String json) {
    List<Map<String, Object>> jsonList = JsonConverter.readValue(json);
    return jsonList.stream().map(mapper::toSugerenciaInscripcion).collect(Collectors.toList());
  }

  private String extraerJson(String sugerencia) {
    int inicio = sugerencia.indexOf("textContent=[") + "textContent=[".length();
    int fin = sugerencia.lastIndexOf("], metadata=");

    if (inicio >= 0 && fin > inicio) {
      String jsonArray = sugerencia.substring(inicio, fin).trim();
      return "[" + jsonArray + "]";
    } else {
      throw new RuntimeException("No se pudo encontrar el array de textContent");
    }
  }
}