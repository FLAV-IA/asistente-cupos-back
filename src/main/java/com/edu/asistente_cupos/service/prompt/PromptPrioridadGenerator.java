package com.edu.asistente_cupos.service.prompt;

import com.edu.asistente_cupos.Utils.JsonConverter;
import com.edu.asistente_cupos.Utils.prompt.PromptTemplateProvider;
import com.edu.asistente_cupos.domain.peticion.PeticionInscripcion;
import com.edu.asistente_cupos.domain.prompt.PromptBuilderTemplated;
import com.edu.asistente_cupos.domain.prompt.optimizado.PeticionInscripcion4Prompt;
import com.edu.asistente_cupos.mapper.PeticionInscripcionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PromptPrioridadGenerator implements PromptGenerator<List<PeticionInscripcion>> {
  protected static final String EJEMPLO_JSON = """
    [
      {
        "a": "12345678",
        "h": {
          "i": 22,
          "ap": 20,
          "cf": 8.3,
          "ca": ["MAT1", "ALG1"],
          "ac": ["PROG2"]
        },
        "p": [
          {
            "n": 1036,
            "m": ["MAT1-01", "MAT1-02"],
            "c": true
          },
          {
            "n": 1035,
            "m": ["ALG1-01"],
            "c": false
          }
        ]
      }
    ]
    """;
  protected static final String EJEMPLO_OUTPUT = """
    [
      {
          "a": "12345678",
          "ep": [
            {
              "n": "1035",
              "p": 91,
              "e": ["COR", "AVZ"]
            },
            {
              "n": "1036",
              "p": 78,
              "e": ["REZ", "REC"]
            }
          ]
        },
    ]
    
    """;
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
                                 .conVariable("ejemploJson", EJEMPLO_JSON)
                                 .conVariable("ejemploOutput", EJEMPLO_OUTPUT).construir();

  }
}
