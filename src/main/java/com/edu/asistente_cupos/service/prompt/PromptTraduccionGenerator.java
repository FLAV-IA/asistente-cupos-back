package com.edu.asistente_cupos.service.prompt;

import com.edu.asistente_cupos.Utils.JsonConverter;
import com.edu.asistente_cupos.Utils.prompt.PromptTemplateProvider;
import com.edu.asistente_cupos.domain.prompt.PromptBuilderTemplated;
import com.edu.asistente_cupos.domain.prompt.optimizado.SugerenciaParaTraducir4Prompt;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaInscripcion;
import com.edu.asistente_cupos.mapper.SugerenciaInscripcionPromptMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PromptTraduccionGenerator implements PromptGenerator<List<SugerenciaInscripcion>> {
  protected static final String EJEMPLO_INPUT = """
    [
      {
        "peticion": {
          "estudiante": { "nombre": "Juan" },
          "comision": { "codigo": "MAT1-01-c3", "materia": { "nombre": "Matemática I" } },
          "prioridad": 90,
          "motivo": "AVZ, COR, CF"
        },
        "cupoAsignado": true
      }
    ]
    """;
  protected static final String EJEMPLO_OUTPUT = """
    [
      {
        "a": "12312334",
        "c": "MAT1-01-c3",
        "p": 90,
        "m": "Está por terminar la carrera, cumple correlativas y tiene un buen promedio.",
        "x": true
      }
    ]
    """;
  private final PromptTemplateProvider templateProvider;
  private final SugerenciaInscripcionPromptMapper promptMapper;

  @Override
  public Prompt crearPrompt(List<SugerenciaInscripcion> sugerencias) {
    List<SugerenciaParaTraducir4Prompt> simplificadas = promptMapper.toPromptList(sugerencias);

    return PromptBuilderTemplated.nuevo(templateProvider)
                                 .conSystemTemplate("prompt/traduccion/system.txt")
                                 .conUserTemplate("prompt/traduccion/user.txt")
                                 .conVariable("peticiones", JsonConverter.toJson(simplificadas))
                                 .conVariable("ejemploInput", EJEMPLO_INPUT)
                                 .conVariable("ejemploOutput", EJEMPLO_OUTPUT).construir();
  }
}
