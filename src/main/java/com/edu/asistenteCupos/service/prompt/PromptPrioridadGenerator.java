package com.edu.asistenteCupos.service.prompt;

import com.edu.asistenteCupos.Utils.JsonConverter;
import com.edu.asistenteCupos.Utils.prompt.PromptTemplateProvider;
import com.edu.asistenteCupos.domain.PeticionInscripcion;
import com.edu.asistenteCupos.domain.prompt.PromptBuilderTemplated;
import com.edu.asistenteCupos.domain.prompt.optimizado.PeticionInscripcion4Prompt;
import com.edu.asistenteCupos.mapper.PeticionInscripcionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PromptPrioridadGenerator implements PromptGenerator<List<PeticionInscripcion>> {
  public static final String EJEMPLO_JSON = """
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
            "m": ["MAT1-01", "MAT1-02"],
            "c": true
          },
          {
            "m": ["ALG1-01"],
            "c": false
          }
        ]
      }
    ]
    """;
  public static final String EJEMPLO_OUTPUT = """
    [
      {
        "a": "12345678",
        "m": "MAT1",
        "p": 86,
        "e": ["COR", "REC", "CF"]
      },
      {
        "a": "12345678",
        "m": "ALG1",
        "p": 55,
        "e": ["SIN", "REZ"]
      }
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
