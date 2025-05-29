package com.edu.asistente_cupos.controller;

import com.edu.asistente_cupos.assembler.EnsambladorDePeticiones;
import com.edu.asistente_cupos.controller.dto.PeticionInscripcionCsvDTO;
import com.edu.asistente_cupos.controller.dto.SugerenciaInscripcionDTO;
import com.edu.asistente_cupos.domain.filtros.FiltroDePeticionInscripcion;
import com.edu.asistente_cupos.domain.peticion.PeticionInscripcion;
import com.edu.asistente_cupos.mapper.SugerenciaInscripcionMapper;
import com.edu.asistente_cupos.service.AsistenteDeInscripcion;
import com.edu.asistente_cupos.service.adapter.PeticionInscripcionCsvAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AsistenteControllerTest {
  private MockMvc mockMvc;
  private AsistenteDeInscripcion asistenteDeInscripcion;
  private SugerenciaInscripcionMapper sugerenciaInscripcionMapper;
  private PeticionInscripcionCsvAdapter peticionInscripcionCsvAdapter;
  private EnsambladorDePeticiones ensambladorDePeticiones;
  private FiltroDePeticionInscripcion filtroDePeticionInscripcion;
  @BeforeEach
  void setUp() {
    asistenteDeInscripcion = mock(AsistenteDeInscripcion.class);
    sugerenciaInscripcionMapper = mock(SugerenciaInscripcionMapper.class);
    peticionInscripcionCsvAdapter = mock(PeticionInscripcionCsvAdapter.class);
    ensambladorDePeticiones = mock(EnsambladorDePeticiones.class);
    filtroDePeticionInscripcion = mock(FiltroDePeticionInscripcion.class);


    AsistenteController controller = new AsistenteController(asistenteDeInscripcion,
      sugerenciaInscripcionMapper, peticionInscripcionCsvAdapter, ensambladorDePeticiones,filtroDePeticionInscripcion);

    mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
  }

  @Test
  void retornaSugerenciasDTOCuandoCsvEsValido() throws Exception {
    MockMultipartFile archivo = new MockMultipartFile("file", "peticiones.csv", "text/csv",
      "dni|codigos_comisiones\n1234|MAT1-01".getBytes());

    PeticionInscripcionCsvDTO dto = new PeticionInscripcionCsvDTO();
    dto.setDni("1234");
    dto.setCodigosComisiones("MAT1-01");

    PeticionInscripcion peticion = PeticionInscripcion.builder().estudiante(null)
                                                      .peticionPorMaterias(List.of()).build();

    SugerenciaInscripcionDTO dtoFinal = SugerenciaInscripcionDTO.builder().nombreEstudiante("Juan")
                                                                .dniEstudiante("1234")
                                                                .nombreMateria("Matemática")
                                                                .codigoComision("MAT1-01")
                                                                .motivo("AVZ").prioridad(90)
                                                                .cupoAsignado(true).build();

    when(peticionInscripcionCsvAdapter.adapt(any())).thenReturn(List.of(dto));
    when(ensambladorDePeticiones.ensamblarDesdeCsvDto(any())).thenReturn(List.of(peticion));
    when(asistenteDeInscripcion.sugerirInscripcion(any())).thenReturn(List.of());
    when(sugerenciaInscripcionMapper.toSugerenciaInscripcionDtoList(any())).thenReturn(
      List.of(dtoFinal));

    mockMvc.perform(multipart("/asistente/sugerencia-inscripcion-con-csv").file(archivo)
                                                                          .contentType(
                                                                            MediaType.MULTIPART_FORM_DATA))
           .andExpect(status().isOk());
  }

  @Test
  void retorna400SiNoSeEnvioArchivo() throws Exception {
    mockMvc.perform(multipart("/asistente/sugerencia-inscripcion-con-csv"))
           .andExpect(status().isBadRequest());
  }
}