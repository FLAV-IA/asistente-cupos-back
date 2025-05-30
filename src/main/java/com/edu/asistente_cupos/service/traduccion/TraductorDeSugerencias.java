package com.edu.asistente_cupos.service.traduccion;

import com.edu.asistente_cupos.domain.sugerencia.SugerenciaInscripcion;
import com.edu.asistente_cupos.service.llm.LlmClient;
import com.edu.asistente_cupos.service.llm.RespuestaLLMParser;
import com.edu.asistente_cupos.service.prompt.PromptGenerator;
import com.edu.asistente_cupos.service.traduccion.dto.SugerenciaInscripcionLLM;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TraductorDeSugerencias {
  private final PromptGenerator<List<SugerenciaInscripcion>> promptGenerator;
  private final LlmClient llmClient;
  private final RespuestaLLMParser respuestaLLMParser;

  public List<SugerenciaInscripcionLLM> traducir(List<SugerenciaInscripcion> sugerencias) {
    Prompt prompt = promptGenerator.crearPrompt(sugerencias);
    ChatResponse respuesta = llmClient.call(prompt);
    return respuestaLLMParser.parsear(respuesta,
      new TypeReference<List<SugerenciaInscripcionLLM>>() {});
  }
}