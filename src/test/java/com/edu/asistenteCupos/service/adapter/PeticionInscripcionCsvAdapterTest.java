package com.edu.asistenteCupos.service.adapter;

import com.edu.asistenteCupos.controller.dto.PeticionInscripcionCsvDTO;
import com.edu.asistenteCupos.domain.Comision;
import com.edu.asistenteCupos.domain.Estudiante;
import com.edu.asistenteCupos.domain.Materia;
import com.edu.asistenteCupos.domain.PeticionInscripcion;
import com.edu.asistenteCupos.mapper.PeticionInscripcionMapper;
import com.edu.asistenteCupos.mapper.PeticionInscripcionMappingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PeticionInscripcionCsvAdapterTest {
  private PeticionInscripcionMappingService mappingService;
  private PeticionInscripcionMapper peticionInscripcionMapper;
  private PeticionInscripcionCsvAdapter adapter;

  @BeforeEach
  void setUp() {
    mappingService = mock(PeticionInscripcionMappingService.class);
    peticionInscripcionMapper = mock(PeticionInscripcionMapper.class);
    adapter = new PeticionInscripcionCsvAdapter(peticionInscripcionMapper, mappingService);
  }

  @Test
  void testConvertirArchivoCsvCorrectamente() throws IOException {
    String csvContent = "legajo,codigoComision\n12345,COM01\n";
    MockMultipartFile archivoCsv = new MockMultipartFile("archivo", "peticiones.csv", "text/csv",
      new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8)));

    PeticionInscripcionCsvDTO dto = new PeticionInscripcionCsvDTO();
    dto.setLegajo("12345");
    dto.setCodigoComision("COM01");

    PeticionInscripcion peticion = PeticionInscripcion.builder().estudiante(
                                                        Estudiante.builder().legajo("12345").nombre("Juan Perez").build()).materia("MAT01")
                                                      .comisiones(Collections.singletonList(
                                                        Comision.builder().codigo("COM01")
                                                                .horario("Lunes 10hs").materia(
                                                                  Materia.builder().codigo("MAT01")
                                                                         .nombre("Matemática 1").build())
                                                                .cupo(30).build()))
                                                      .cumpleCorrelativa(false).build();

    when(peticionInscripcionMapper.toPeticionInscripcionCsvList(anyList(),
      eq(mappingService))).thenReturn(List.of(peticion));


    List<PeticionInscripcion> resultados = adapter.convertir(archivoCsv);


    assertNotNull(resultados);
    assertEquals(1, resultados.size());
    PeticionInscripcion resultado = resultados.get(0);
    assertEquals("12345", resultado.getEstudiante().getLegajo());
    assertEquals("MAT01", resultado.getMateria());
    assertEquals(1, resultado.getComisiones().size());
    assertEquals("COM01", resultado.getComisiones().get(0).getCodigo());
    assertFalse(resultado.isCumpleCorrelativa());
  }

  @Test
  void testErrorAlProcesarArchivoCsv() throws IOException {
    MultipartFile archivoCsv = mock(MultipartFile.class);
    when(archivoCsv.getInputStream()).thenThrow(new IOException("fallo de lectura"));

    RuntimeException ex = assertThrows(RuntimeException.class, () -> adapter.convertir(archivoCsv));
    assertTrue(ex.getMessage().contains("Error al procesar el archivo CSV"));
    assertInstanceOf(IOException.class, ex.getCause());
  }
}
