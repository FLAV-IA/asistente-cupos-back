package com.edu.asistenteCupos.domain;

import com.edu.asistenteCupos.Utils.PromptTemplateProvider;
import com.edu.asistenteCupos.domain.prompt.PromptBuilderTemplated;
import com.edu.asistenteCupos.mapper.PeticionInscripcionMapper;
import com.edu.asistenteCupos.utils.GeneradorDeDatosDePrueba;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class PromptBuilderTemplatedTest {
  Function<String, String> normalizarString;
  private PromptTemplateProvider promptTemplateProvider;
  private PromptBuilderTemplated promptBuilder;
  private List<Materia> materias;
  private List<Comision> comisiones;
  private List<PeticionInscripcion> peticiones;

  @BeforeEach
  void setUp() {
    promptTemplateProvider = Mockito.mock(PromptTemplateProvider.class);
    promptBuilder = PromptBuilderTemplated.nuevo(promptTemplateProvider);

    Materia matematica = new Materia("Matemáticas", "MAT101", List.of());
    Materia fisica = new Materia("Física", "FIS201", List.of());
    materias = Arrays.asList(matematica, fisica);

    comisiones = Arrays.asList(new Comision("COM001","Lunes 10:00", 1, matematica),
      new Comision("COM002", "Martes 14:00", 3, fisica));

    peticiones = GeneradorDeDatosDePrueba.peticionInscripcionesDePrueba();
    normalizarString = s -> s.replace("\r\n", "\n").replaceAll("\\s+$", "").trim();
  }

  @Test
  void construirEntregaUnPromptCorrecto() {
    PeticionInscripcionMapper mapper = Mappers.getMapper(PeticionInscripcionMapper.class);
    String systemPromptContent = "System prompt content";
    String userPromptContent = "User prompt content with {peticiones} with {materias} and {comisiones}";
    Resource userResource = new ByteArrayResource(userPromptContent.getBytes());
    when(promptTemplateProvider.metodoNelson()).thenReturn(systemPromptContent);
    when(promptTemplateProvider.userResource()).thenReturn(userResource);


    Prompt prompt = promptBuilder.conMaterias(materias).conComisiones(comisiones)
                                 .conPeticionesDeInscripcion(peticiones).construir();


    String resultado =
      "[ " + peticiones.stream().map(mapper::toJson).collect(Collectors.joining(", ")) + " ]";
    assertEquals(2, prompt.getInstructions().size());
    Message systemMessage = prompt.getInstructions().get(0);
    Message userMessage = prompt.getInstructions().get(1);
    String expectedSystemContent = "System prompt content";
    String expectedUserContent = "User prompt content with " + resultado +
      " with - MAT101 (Matemáticas)\n- FIS201 (Física) and - COM001 (cupo: 1)\n- COM002 (cupo: 3)";

    assertEquals(expectedSystemContent, systemMessage.getText());
    String expected = this.normalizarString.apply(expectedUserContent);
    String actuakl = this.normalizarString.apply(userMessage.getText());
    assertEquals(expected, actuakl);
  }
}