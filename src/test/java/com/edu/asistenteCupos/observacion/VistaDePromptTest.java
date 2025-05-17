package com.edu.asistenteCupos.observacion;

import com.edu.asistenteCupos.domain.peticion.PeticionInscripcion;
import com.edu.asistenteCupos.domain.prompt.PromptPrinter;
import com.edu.asistenteCupos.service.prompt.PromptGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.prompt.Prompt;

import java.util.List;

import static com.edu.asistenteCupos.testutils.TestDataFactory.crearPeticionInscripcionDummy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class VistaDePromptTest {

  @Test
  void debe_devolver_prompt_formateado_bonito() {
    PromptGenerator<List<PeticionInscripcion>> mockGenerator = mock(PromptGenerator.class);
    VistaDePrompt vista = new VistaDePrompt(mockGenerator);

    List<PeticionInscripcion> input = List.of(crearPeticionInscripcionDummy());
    Prompt prompt = new Prompt("contenido del prompt");

    when(mockGenerator.crearPrompt(input)).thenReturn(prompt);


    String bonito = vista.mostrarPromptBonito(input);


    assertThat(bonito).isEqualTo(PromptPrinter.imprimir(prompt, true));
    verify(mockGenerator).crearPrompt(input);
  }

  @Test
  void debe_devolver_prompt_formateado_crudo() {
    PromptGenerator<List<PeticionInscripcion>> mockGenerator = mock(PromptGenerator.class);
    VistaDePrompt vista = new VistaDePrompt(mockGenerator);

    List<PeticionInscripcion> input = List.of(crearPeticionInscripcionDummy());
    Prompt prompt = new Prompt("contenido del prompt");

    when(mockGenerator.crearPrompt(input)).thenReturn(prompt);


    String crudo = vista.mostrarPromptCrudo(input);


    assertThat(crudo).isEqualTo(PromptPrinter.imprimir(prompt, false));
    verify(mockGenerator).crearPrompt(input);
  }
}
