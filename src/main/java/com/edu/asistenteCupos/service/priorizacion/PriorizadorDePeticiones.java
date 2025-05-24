package com.edu.asistenteCupos.service.priorizacion;

import com.edu.asistenteCupos.domain.peticion.PeticionInscripcion;
import com.edu.asistenteCupos.service.llm.LlmClient;
import com.edu.asistenteCupos.service.llm.RespuestaLLMParser;
import com.edu.asistenteCupos.service.priorizacion.dto.ResultadoPriorizacionLLM;
import com.edu.asistenteCupos.service.prompt.PromptGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PriorizadorDePeticiones {
  private final PromptGenerator<List<PeticionInscripcion>> promptGenerator;
  private final LlmClient llmClient;
  private final RespuestaLLMParser respuestaLLMParser;

  public List<ResultadoPriorizacionLLM> priorizar(List<PeticionInscripcion> peticiones) {
    Prompt prompt = promptGenerator.crearPrompt(peticiones);
    log.info("Prompt generado: {}", prompt);
    ChatResponse respuesta = llmClient.call(prompt);
    return respuestaLLMParser.parsear(respuesta,
      new TypeReference<List<ResultadoPriorizacionLLM>>() {});
  }
}
