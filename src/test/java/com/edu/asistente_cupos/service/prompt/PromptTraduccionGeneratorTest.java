package com.edu.asistente_cupos.service.prompt;

import com.edu.asistente_cupos.Utils.prompt.PromptTemplateProvider;
import com.edu.asistente_cupos.domain.prompt.optimizado.SugerenciaParaTraducir4Prompt;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaInscripcion;
import com.edu.asistente_cupos.mapper.SugerenciaInscripcionPromptMapper;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.core.io.Resource;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.edu.asistente_cupos.testutils.TestDataFactory.crearSugerenciaAsignadaDummy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PromptTraduccionGeneratorTest {
  @Test
  void generaPromptConTemplatesYVariablesCorrectas() throws Exception {
    PromptTemplateProvider templateProvider = mock(PromptTemplateProvider.class);
    SugerenciaInscripcionPromptMapper promptMapper = mock(SugerenciaInscripcionPromptMapper.class);
    PromptTraduccionGenerator generator = new PromptTraduccionGenerator(templateProvider,
      promptMapper);

    List<SugerenciaInscripcion> sugerencias = List.of(crearSugerenciaAsignadaDummy());
    List<SugerenciaParaTraducir4Prompt> simplificadas = List.of(
      mock(SugerenciaParaTraducir4Prompt.class));

    when(promptMapper.toPromptList(sugerencias)).thenReturn(simplificadas);

    when(templateProvider.leerTemplateComoString("prompt/traduccion/system.txt")).thenReturn(
      "SYSTEM TEMPLATE");

    Resource resourceMock = mock(Resource.class);
    when(resourceMock.getInputStream()).thenReturn(
      new ByteArrayInputStream("USER TEMPLATE {{peticiones}}".getBytes(StandardCharsets.UTF_8)));
    when(templateProvider.getTemplate("prompt/traduccion/user.txt")).thenReturn(resourceMock);


    Prompt prompt = generator.crearPrompt(sugerencias);


    assertThat(prompt).isNotNull();
    assertThat(prompt.getInstructions()).hasSize(2);
    verify(promptMapper).toPromptList(sugerencias);
    verify(templateProvider).leerTemplateComoString("prompt/traduccion/system.txt");
    verify(templateProvider).getTemplate("prompt/traduccion/user.txt");
  }
}
