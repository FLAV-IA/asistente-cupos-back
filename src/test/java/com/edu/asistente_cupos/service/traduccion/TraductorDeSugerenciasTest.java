package com.edu.asistente_cupos.service.traduccion;

import com.edu.asistente_cupos.domain.sugerencia.SugerenciaInscripcion;
import com.edu.asistente_cupos.service.llm.LlmClient;
import com.edu.asistente_cupos.service.llm.RespuestaLLMParser;
import com.edu.asistente_cupos.service.prompt.PromptGenerator;
import com.edu.asistente_cupos.service.traduccion.dto.SugerenciaInscripcionLLM;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;

import java.util.List;

import static com.edu.asistente_cupos.testutils.TestDataFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TraductorDeSugerenciasTest {

  @Test
  void traduceSugerenciasYDevuelveRespuestaDelLlm() {
    PromptGenerator<List<SugerenciaInscripcion>> promptGenerator = mock(PromptGenerator.class);
    LlmClient llmClient = mock(LlmClient.class);
    RespuestaLLMParser respuestaLLMParser = mock(RespuestaLLMParser.class);

    TraductorDeSugerencias traductor = new TraductorDeSugerencias(promptGenerator, llmClient, respuestaLLMParser);

    List<SugerenciaInscripcion> sugerencias = List.of(crearSugerenciaAsignadaDummy());
    List<SugerenciaInscripcionLLM> esperadas = List.of(crearSugerenciaLLMDummy());

    Prompt promptMock = new Prompt("PROMPT PARA LLM");
    ChatResponse respuestaMock = new ChatResponse(List.of());

    when(promptGenerator.crearPrompt(sugerencias)).thenReturn(promptMock);
    when(llmClient.call(promptMock)).thenReturn(respuestaMock);
    when(respuestaLLMParser.parsear(eq(respuestaMock), any(TypeReference.class))).thenReturn(esperadas);


    List<SugerenciaInscripcionLLM> resultado = traductor.traducir(sugerencias);


    assertThat(resultado).isEqualTo(esperadas);
    verify(promptGenerator).crearPrompt(sugerencias);
    verify(llmClient).call(promptMock);
    verify(respuestaLLMParser).parsear(eq(respuestaMock), any(TypeReference.class));
  }
}
