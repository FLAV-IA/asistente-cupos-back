package com.edu.asistenteCupos.service.prompt;

import com.edu.asistenteCupos.Utils.JsonConverter;
import com.edu.asistenteCupos.Utils.prompt.PromptTemplateProvider;
import com.edu.asistenteCupos.domain.PeticionInscripcion;
import com.edu.asistenteCupos.domain.prompt.PromptBuilderTemplated;
import com.edu.asistenteCupos.mapper.PeticionInscripcionMapper;
import com.edu.asistenteCupos.domain.prompt.optimizado.PeticionInscripcion4Prompt;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PromptPrioridadGenerator implements PromptGenerator<List<PeticionInscripcion>> {
  private final PromptTemplateProvider templateProvider;
  private final PeticionInscripcionMapper peticionMapper;

  @Override
  public Prompt crearPrompt(List<PeticionInscripcion> peticiones) {
    List<PeticionInscripcion4Prompt> peticiones4Prompt = peticionMapper.toPeticionInscripcion4PromptList(
      peticiones);

    return PromptBuilderTemplated.nuevo(templateProvider)
                                 .conSystemTemplate("prompt/priorizacion/system.txt")
                                 .conUserTemplate("prompt/priorizacion/user.txt")
                                 .conVariable("peticiones", JsonConverter.toJson(peticiones4Prompt))
                                 .construir();
  }
}
