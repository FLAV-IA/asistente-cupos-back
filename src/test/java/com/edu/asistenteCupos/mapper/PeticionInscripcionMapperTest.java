package com.edu.asistenteCupos.mapper;

import com.edu.asistenteCupos.controller.dto.PeticionInscripcionCsvDTO;
import com.edu.asistenteCupos.domain.Comision;
import com.edu.asistenteCupos.domain.Estudiante;
import com.edu.asistenteCupos.domain.Materia;
import com.edu.asistenteCupos.domain.PeticionInscripcion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PeticionInscripcionMapperTest {
  private PeticionInscripcionMapper mapper;
  private PeticionInscripcionMappingService mappingService;

  @BeforeEach
  void setUp() {
    mapper = Mappers.getMapper(PeticionInscripcionMapper.class);
    mappingService = mock(PeticionInscripcionMappingService.class);
  }

  @Test
  void convierteUnaPeticionInscripcionCsvDTOAUnaPeticionInscripcion() {
    PeticionInscripcionCsvDTO csvDto = new PeticionInscripcionCsvDTO();
    csvDto.setDni("22222");
    csvDto.setCodigosComisiones("COM02");
    csvDto.setCodigoMateria("MAT02");

    Estudiante estudiante = Estudiante.builder().dni("22222").nombre("Laura").build();
    Materia materia = Materia.builder().codigo("MAT02").nombre("Fisica").build();
    Comision comision = Comision.builder().codigo("COM02").materia(materia).build();

    when(mappingService.buscarEstudiantePorDni("22222")).thenReturn(estudiante);
    when(mappingService.buscarComisionPorCodigo("COM02")).thenReturn(comision);
    when(mappingService.buscarMateriaPorCodigo("MAT02")).thenReturn(materia);


    PeticionInscripcion peticion = mapper.toPeticionInscripcion(csvDto, mappingService);


    assertEquals("22222", peticion.getEstudiante().getDni());
    assertEquals("MAT02", peticion.getMateria().getCodigo());
    assertEquals(1, peticion.getComisiones().size());
    assertEquals("COM02", peticion.getComisiones().get(0).getCodigo());
    assertFalse(peticion.isCumpleCorrelativa());
  }

  @Test
  void convierteListaDePeticionInscripcionCsvDTOAPeticionesDeInscripcion() {
    PeticionInscripcionCsvDTO csvDto = new PeticionInscripcionCsvDTO();
    csvDto.setDni("11111");
    csvDto.setCodigoMateria("MAT01");
    csvDto.setCodigosComisiones("COM01, COM02");

    Estudiante estudiante = Estudiante.builder().dni("11111").nombre("Pepe").build();
    Materia materia = Materia.builder().codigo("MAT01").nombre("Matematica").build();
    Comision comision = Comision.builder().codigo("COM01").materia(materia).build();
    Comision comision2 = Comision.builder().codigo("COM02").materia(materia).build();

    when(mappingService.buscarEstudiantePorDni("11111")).thenReturn(estudiante);
    when(mappingService.buscarComisionPorCodigo("COM01")).thenReturn(comision);
    when(mappingService.buscarComisionPorCodigo("COM02")).thenReturn(comision2);
    when(mappingService.buscarMateriaPorCodigo("MAT01")).thenReturn(materia);


    List<PeticionInscripcion> peticiones = mapper.toPeticionInscripcionCsvList(List.of(csvDto),
      mappingService);


    assertThat(peticiones).hasSize(1);
    PeticionInscripcion peticion = peticiones.get(0);
    assertEquals("11111", peticion.getEstudiante().getDni());
    assertEquals("MAT01", peticion.getMateria().getCodigo());
    assertEquals(2, peticion.getComisiones().size());
    assertEquals("COM01", peticion.getComisiones().get(0).getCodigo());
    assertEquals("COM02", peticion.getComisiones().get(1).getCodigo());
    assertFalse(peticion.isCumpleCorrelativa());
  }

  @Test
  void conviertePeticionInscripcionAJson() {

    Estudiante estudiante = Estudiante.builder().nombre("Tomas").dni("7890").build();
    Materia materia = Materia.builder().codigo("MAT01").build();
    PeticionInscripcion peticion = PeticionInscripcion.builder().materia(materia)
                                                      .cumpleCorrelativa(true)
                                                      .estudiante(estudiante).build();

    String json = mapper.toJson(peticion);

    assertThat(json).contains("Tomas");
    assertThat(json).contains("7890");
    assertThat(json).contains("MAT01");
  }

  @Test
  void lanzaUnErrorSiFallaLaSerializacionAJson() {
    PeticionInscripcion peticion = mock(PeticionInscripcion.class);
    when(peticion.getMateria()).thenThrow(new RuntimeException("Fallo interno"));

    RuntimeException ex = assertThrows(RuntimeException.class, () -> mapper.toJson(peticion));
    assertThat(ex.getMessage()).contains("Error al convertir PeticionInscripcion a JSON");
  }
}
