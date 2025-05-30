package com.edu.asistente_cupos.service.priorizacion;

import com.edu.asistente_cupos.domain.peticion.PeticionInscripcion;
import com.edu.asistente_cupos.service.llm.LlmClient;
import com.edu.asistente_cupos.service.llm.RespuestaLLMParser;
import com.edu.asistente_cupos.service.priorizacion.dto.ResultadoPriorizacionLLM;
import com.edu.asistente_cupos.service.prompt.PromptGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;

import java.util.List;

import static com.edu.asistente_cupos.testutils.TestDataFactory.crearPeticionInscripcionDummy;
import static com.edu.asistente_cupos.testutils.TestDataFactory.crearResultadoPriorizacionLLMDummy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PriorizadorDePeticionesTest {
  @Test
  void priorizaPeticionesCorrectamente() {
    PromptGenerator<List<PeticionInscripcion>> promptGenerator = mock(PromptGenerator.class);
    LlmClient llmClient = mock(LlmClient.class);
    RespuestaLLMParser respuestaLLMParser = mock(RespuestaLLMParser.class);

    PriorizadorDePeticiones priorizador = new PriorizadorDePeticiones(promptGenerator, llmClient,
      respuestaLLMParser);

    List<PeticionInscripcion> peticiones = List.of(crearPeticionInscripcionDummy());
    Prompt prompt = new Prompt("PROMPT TEST");
    ChatResponse respuestaLLM = new ChatResponse(List.of());

    List<ResultadoPriorizacionLLM> resultadoEsperado = List.of(
      crearResultadoPriorizacionLLMDummy());

    when(promptGenerator.crearPrompt(peticiones)).thenReturn(prompt);
    when(llmClient.call(prompt)).thenReturn(respuestaLLM);
    when(respuestaLLMParser.parsear(eq(respuestaLLM), any(TypeReference.class))).thenReturn(
      resultadoEsperado);


    List<ResultadoPriorizacionLLM> resultado = priorizador.priorizar(peticiones);


    assertThat(resultado).isEqualTo(resultadoEsperado);
    verify(promptGenerator).crearPrompt(peticiones);
    verify(llmClient).call(prompt);
    verify(respuestaLLMParser).parsear(eq(respuestaLLM), any());
  }
}
