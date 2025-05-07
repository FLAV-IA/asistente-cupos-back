package com.edu.asistenteCupos.service.prompt;

import com.edu.asistenteCupos.Utils.JsonConverter;
import com.edu.asistenteCupos.Utils.prompt.PromptTemplateProvider;
import com.edu.asistenteCupos.domain.prompt.PromptBuilderTemplated;
import com.edu.asistenteCupos.mapper.SugerenciaInscripcionPromptMapper;
import com.edu.asistenteCupos.domain.sugerencia.SugerenciaInscripcion;
import com.edu.asistenteCupos.domain.prompt.optimizado.SugerenciaParaTraducir4Prompt;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PromptTraduccionGenerator implements PromptGenerator<List<SugerenciaInscripcion>> {
  private final PromptTemplateProvider templateProvider;
  private final SugerenciaInscripcionPromptMapper promptMapper;

  @Override
  public Prompt crearPrompt(List<SugerenciaInscripcion> sugerencias) {
    List<SugerenciaParaTraducir4Prompt> simplificadas = promptMapper.toPromptList(sugerencias);

    return PromptBuilderTemplated.nuevo(templateProvider)
                                 .conSystemTemplate("prompt/traduccion/system.txt")
                                 .conUserTemplate("prompt/traduccion/user.txt")
                                 .conVariable("peticiones", JsonConverter.toJson(simplificadas))
                                 .construir();
  }
}

