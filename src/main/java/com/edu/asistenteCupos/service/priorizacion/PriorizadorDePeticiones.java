package com.edu.asistenteCupos.service.priorizacion;

import com.edu.asistenteCupos.Utils.JsonConverter;
import com.edu.asistenteCupos.domain.PeticionInscripcion;
import com.edu.asistenteCupos.mapper.SugerenciaInscripcionMapper;
import com.edu.asistenteCupos.service.llm.LlmClient;
import com.edu.asistenteCupos.service.llm.RespuestaLLMParser;
import com.edu.asistenteCupos.service.prompt.PromptGenerator;
import com.edu.asistenteCupos.service.priorizacion.dto.ResultadoPriorizacionLLM;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PriorizadorDePeticiones {
  private final PromptGenerator<List<PeticionInscripcion>> promptGenerator;
  private final LlmClient llmClient;
  private final RespuestaLLMParser respuestaLLMParser;
  private SugerenciaInscripcionMapper mapper;

  public List<ResultadoPriorizacionLLM> priorizar(List<PeticionInscripcion> peticiones) {
    Prompt prompt = promptGenerator.crearPrompt(peticiones);
    ChatResponse respuesta = llmClient.call(prompt);
    return respuestaLLMParser.parsear(respuesta,
      x -> JsonConverter.convertMap(x, ResultadoPriorizacionLLM.class));
  }
}
