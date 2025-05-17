package com.edu.asistenteCupos.service.prompt;

import com.edu.asistenteCupos.Utils.prompt.PromptTemplateProvider;
import com.edu.asistenteCupos.domain.peticion.PeticionInscripcion;
import com.edu.asistenteCupos.domain.prompt.optimizado.PeticionInscripcion4Prompt;
import com.edu.asistenteCupos.mapper.PeticionInscripcionMapper;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.core.io.Resource;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.edu.asistenteCupos.testutils.TestDataFactory.crearPeticionInscripcionDummy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PromptPrioridadGeneratorTest {
  @Test
  void generaPromptConVariablesJsonYTemplates() throws Exception {
    PromptTemplateProvider templateProvider = mock(PromptTemplateProvider.class);
    PeticionInscripcionMapper peticionMapper = mock(PeticionInscripcionMapper.class);
    PromptPrioridadGenerator generator = new PromptPrioridadGenerator(templateProvider,
      peticionMapper);

    List<PeticionInscripcion> peticiones = List.of(crearPeticionInscripcionDummy());
    List<PeticionInscripcion4Prompt> peticiones4Prompt = List.of(
      mock(PeticionInscripcion4Prompt.class));

    when(peticionMapper.toPeticionInscripcion4PromptList(peticiones)).thenReturn(peticiones4Prompt);

    when(templateProvider.leerTemplateComoString("prompt/priorizacion/system.txt")).thenReturn(
      "SYSTEM TEMPLATE");

    Resource userTemplateMock = mock(Resource.class);
    when(userTemplateMock.getInputStream()).thenReturn(new java.io.ByteArrayInputStream(
      "USER TEMPLATE {{peticiones}}".getBytes(StandardCharsets.UTF_8)));
    when(templateProvider.getTemplate("prompt/priorizacion/user.txt")).thenReturn(userTemplateMock);


    Prompt prompt = generator.crearPrompt(peticiones);


    assertThat(prompt).isNotNull();
    assertThat(prompt.getInstructions()).hasSize(2);
  }
}
