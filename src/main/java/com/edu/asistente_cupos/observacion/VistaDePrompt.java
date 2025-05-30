package com.edu.asistente_cupos.observacion;

import com.edu.asistente_cupos.domain.peticion.PeticionInscripcion;
import com.edu.asistente_cupos.domain.prompt.PromptPrinter;
import com.edu.asistente_cupos.service.prompt.PromptGenerator;
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
