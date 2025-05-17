package com.edu.asistenteCupos.observacion;

import com.edu.asistenteCupos.domain.peticion.PeticionInscripcion;
import com.edu.asistenteCupos.domain.prompt.PromptPrinter;
import com.edu.asistenteCupos.service.prompt.PromptGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class VistaDePrompt {
  private final PromptGenerator<List<PeticionInscripcion>> promptGenerator;

  /**
   * Devuelve el prompt formateado en modo legible para desarrollo.
   */
  public String mostrarPromptBonito(List<PeticionInscripcion> peticiones) {
    Prompt prompt = promptGenerator.crearPrompt(peticiones);
    return PromptPrinter.imprimir(prompt, true);
  }

  /**
   * Devuelve el prompt puro que se enviaría al LLM.
   */
  public String mostrarPromptCrudo(List<PeticionInscripcion> peticiones) {
    Prompt prompt = promptGenerator.crearPrompt(peticiones);
    return PromptPrinter.imprimir(prompt, false);
  }
}
