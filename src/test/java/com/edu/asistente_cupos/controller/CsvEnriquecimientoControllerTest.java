package com.edu.asistente_cupos.controller;

import com.edu.asistente_cupos.assembler.EnsambladorDePeticiones;
import com.edu.asistente_cupos.controller.dto.HistoriaAcademicaDTO;
import com.edu.asistente_cupos.controller.dto.PeticionInscripcionDTO;
import com.edu.asistente_cupos.controller.dto.PeticionPorMateriaDTO;
import com.edu.asistente_cupos.domain.Comision;
import com.edu.asistente_cupos.domain.Estudiante;
import com.edu.asistente_cupos.domain.peticion.PeticionInscripcion;
import com.edu.asistente_cupos.domain.peticion.PeticionPorMateria;
import com.edu.asistente_cupos.excepcion.handler.GlobalExceptionHandler;
import com.edu.asistente_cupos.mapper.PeticionPrevisualizacionMapper;
import com.edu.asistente_cupos.service.adapter.PeticionInscripcionCsvAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CsvEnriquecimientoControllerTest {
  private MockMvc mockMvc;
  private PeticionInscripcionCsvAdapter adapter;
  private EnsambladorDePeticiones ensamblador;
  private PeticionPrevisualizacionMapper mapper;

  @BeforeEach
  void setUp() {
    adapter = mock(PeticionInscripcionCsvAdapter.class);
    ensamblador = mock(EnsambladorDePeticiones.class);
    mapper = mock(PeticionPrevisualizacionMapper.class);

    CsvEnriquecimientoController controller = new CsvEnriquecimientoController(adapter, ensamblador,
      mapper);
    mockMvc = MockMvcBuilders.standaloneSetup(controller)
                             .setControllerAdvice(new GlobalExceptionHandler())
                             .setMessageConverters(new MappingJackson2HttpMessageConverter())
                             .build();
  }

  @Test
  void devuelveDtosCuandoCsvEsValido() throws Exception {
    MockMultipartFile archivo = new MockMultipartFile("file", "peticiones.csv", "text/csv",
      "dni|codigos_comisiones\n12345678|1307-1-CW".getBytes());

    PeticionInscripcion peticion = PeticionInscripcion.builder().estudiante(
      Estudiante.builder().dni("12345678").build()).peticionPorMaterias(List.of(
      PeticionPorMateria.builder()
                        .comisiones(List.of(Comision.builder().codigo("1307-1-CW").build()))
                        .build())).build();

    HistoriaAcademicaDTO historia = new HistoriaAcademicaDTO("12345678", 10, 8, 8.5, true,
      List.of("MAT1"), Set.of("MAT2"));

    PeticionPorMateriaDTO materiaDTO = new PeticionPorMateriaDTO("Álgebra", "MAT3",
      List.of("1307-1-CW"), true);

    PeticionInscripcionDTO dto = new PeticionInscripcionDTO("Juan", "12345678", historia,
      List.of(materiaDTO));

    when(adapter.adapt(any())).thenReturn(List.of());
    when(ensamblador.ensamblarDesdeCsvDto(any())).thenReturn(List.of(peticion));
    when(mapper.toDto(eq(peticion))).thenReturn(dto);

    mockMvc.perform(
             multipart("/csv/previsualizar").file(archivo).contentType(MediaType.MULTIPART_FORM_DATA))
           .andExpect(status().isOk()).andExpect(jsonPath("$[0].nombre").value("Juan"))
           .andExpect(jsonPath("$[0].dni").value("12345678"))
           .andExpect(jsonPath("$[0].materias[0].codigoMateria").value("MAT3")).andExpect(
             jsonPath("$[0].materias[0].codigosComisionesSolicitadas[0]").value("1307-1-CW"));
  }

  @Test
  void retorna400SiNoHayArchivo() throws Exception {
    mockMvc.perform(multipart("/csv/previsualizar")).andExpect(status().isBadRequest());
  }

  @Test
  void retorna400SiArchivoEstaVacio() throws Exception {
    MockMultipartFile archivoVacio = new MockMultipartFile("file", "empty.csv", "text/csv",
      "".getBytes());

    mockMvc.perform(
             multipart("/csv/previsualizar").file(archivoVacio).contentType(MediaType.MULTIPART_FORM_DATA))
           .andExpect(status().isBadRequest());
  }

  @Test
  void devuelveListaVaciaCuandoCsvContieneErroresYNoGeneraPeticiones() throws Exception {
    MockMultipartFile archivoConErrores = new MockMultipartFile("file", "errores.csv", "text/csv",
      "dni|codigos_comisiones\ninvalido|MAT1-01".getBytes());

    when(adapter.adapt(any())).thenReturn(Collections.emptyList());
    when(ensamblador.ensamblarDesdeCsvDto(any())).thenReturn(Collections.emptyList());

    mockMvc.perform(multipart("/csv/previsualizar").file(archivoConErrores)
                                                   .contentType(MediaType.MULTIPART_FORM_DATA))
           .andExpect(status().isOk()).andExpect(jsonPath("$").isEmpty());
  }

  @Test
  void devuelve500CuandoAdapterLanzaExcepcion() throws Exception {
    MockMultipartFile archivo = new MockMultipartFile("file", "peticiones.csv", "text/csv",
      "dni|codigos_comisiones\n1234|MAT1-01".getBytes());

    when(adapter.adapt(any())).thenThrow(new RuntimeException("Error simulado en el adaptador"));

    mockMvc.perform(
             multipart("/csv/previsualizar").file(archivo).contentType(MediaType.MULTIPART_FORM_DATA))
           .andExpect(status().isInternalServerError())
           .andExpect(jsonPath("$.message").value("Error simulado en el adaptador"))
           .andExpect(jsonPath("$.status").value(500));
  }

  @Test
  void devuelve400CuandoAdapterLanzaArchivoInvalido() throws Exception {
    MockMultipartFile archivo = new MockMultipartFile("file", "peticiones.csv", "text/csv",
      "dni|codigos_comisiones\n1234|MAT1-01".getBytes());

    when(adapter.adapt(any())).thenThrow(
      new com.edu.asistente_cupos.excepcion.ArchivoCsvInvalidoException("inv", null));

    mockMvc.perform(
             multipart("/csv/previsualizar").file(archivo).contentType(MediaType.MULTIPART_FORM_DATA))
           .andExpect(status().isBadRequest()).andExpect(jsonPath("$.message").value("inv"))
           .andExpect(jsonPath("$.status").value(400));
  }

  @Test
  void devuelve500CuandoEnsambladorLanzaExcepcion() throws Exception {
    MockMultipartFile archivo = new MockMultipartFile("file", "peticiones.csv", "text/csv",
      "dni|codigos_comisiones\n1234|MAT1-01".getBytes());

    when(adapter.adapt(any())).thenReturn(List.of());
    when(ensamblador.ensamblarDesdeCsvDto(any())).thenThrow(
      new RuntimeException("Error simulado en el ensamblador"));

    mockMvc.perform(
             multipart("/csv/previsualizar").file(archivo).contentType(MediaType.MULTIPART_FORM_DATA))
           .andExpect(status().isInternalServerError())
           .andExpect(jsonPath("$.message").value("Error simulado en el ensamblador"))
           .andExpect(jsonPath("$.status").value(500));
  }

  @Test
  void devuelve500CuandoMapperLanzaExcepcion() throws Exception {
    MockMultipartFile archivo = new MockMultipartFile("file", "peticiones.csv", "text/csv",
      "dni|codigos_comisiones\n1234|MAT1-01".getBytes());

    PeticionInscripcion peticion = PeticionInscripcion.builder().estudiante(null)
                                                      .peticionPorMaterias(List.of()).build();

    when(adapter.adapt(any())).thenReturn(List.of());
    when(ensamblador.ensamblarDesdeCsvDto(any())).thenReturn(List.of(peticion));
    when(mapper.toDto(eq(peticion))).thenThrow(new RuntimeException("Error simulado en el mapper"));

    mockMvc.perform(
             multipart("/csv/previsualizar").file(archivo).contentType(MediaType.MULTIPART_FORM_DATA))
           .andExpect(status().isInternalServerError())
           .andExpect(jsonPath("$.message").value("Error simulado en el mapper"))
           .andExpect(jsonPath("$.status").value(500));
  }

  @Test
  void devuelveOkConListaVaciaSiNoSeGeneranPeticiones() throws Exception {
    MockMultipartFile archivo = new MockMultipartFile("file", "empty_peticiones.csv", "text/csv",
      "dni|codigos_comisiones\n".getBytes());

    when(adapter.adapt(any())).thenReturn(List.of());
    when(ensamblador.ensamblarDesdeCsvDto(any())).thenReturn(Collections.emptyList());

    mockMvc.perform(
             multipart("/csv/previsualizar").file(archivo).contentType(MediaType.MULTIPART_FORM_DATA))
           .andExpect(status().isOk()).andExpect(jsonPath("$").isEmpty());
  }
}