package com.edu.asistente_cupos.controller;

import com.edu.asistente_cupos.assembler.EnsambladorDePeticiones;
import com.edu.asistente_cupos.controller.dto.PeticionInscripcionCsvDTO;
import com.edu.asistente_cupos.domain.peticion.PeticionInscripcion;
import com.edu.asistente_cupos.observacion.VistaDePrompt;
import com.edu.asistente_cupos.service.adapter.PeticionInscripcionCsvAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PromptDebugControllerTest {
  private MockMvc mockMvc;
  private PeticionInscripcionCsvAdapter adapter;
  private EnsambladorDePeticiones ensamblador;
  private VistaDePrompt vista;

  @BeforeEach
  void setUp() {
    adapter = mock(PeticionInscripcionCsvAdapter.class);
    ensamblador = mock(EnsambladorDePeticiones.class);
    vista = mock(VistaDePrompt.class);
    PromptDebugController controller = new PromptDebugController(adapter, ensamblador, vista);
    mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
  }

  @Test
  void retornaPromptFormateadoCuandoElCsvEsValido() throws Exception {
    MockMultipartFile file = new MockMultipartFile("file", "data.csv", "text/csv",
      "contenido".getBytes());
    List<PeticionInscripcionCsvDTO> dtos = List.of(new PeticionInscripcionCsvDTO());
    List<PeticionInscripcion> peticiones = List.of(PeticionInscripcion.builder().build());

    when(adapter.adapt(file)).thenReturn(dtos);
    when(ensamblador.ensamblarDesdeCsvDto(dtos)).thenReturn(peticiones);
    when(vista.mostrarPromptBonito(peticiones)).thenReturn("Prompt generado");

    mockMvc.perform(multipart("/debug/prompt").file(file)).andExpect(status().isOk())
           .andExpect(content().string("Prompt generado"));
  }

  @Test
  void retorna500SiHayErrorEnLaGeneracion() throws Exception {
    MockMultipartFile file = new MockMultipartFile("file", "data.csv", "text/csv",
      "contenido".getBytes());

    when(adapter.adapt(file)).thenThrow(new RuntimeException("error"));

    mockMvc.perform(multipart("/debug/prompt").file(file))
           .andExpect(status().isInternalServerError())
           .andExpect(content().string("Error en la consulta: error"));
  }
}
