package com.edu.asistenteCupos.mapper;

import com.edu.asistenteCupos.controller.dto.HistoriaAcademicaDTO;
import com.edu.asistenteCupos.controller.dto.PeticionInscripcionCsvDTO;
import com.edu.asistenteCupos.controller.dto.PeticionInscriptionDTO;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PeticionInscripcionMapperTest {
  private PeticionInscripcionMapper mapper;
  private PeticionInscripcionMappingService mappingService;

  @BeforeEach void setUp() {
    mapper = Mappers.getMapper(PeticionInscripcionMapper.class);
    mappingService = mock(PeticionInscripcionMappingService.class);
  }

  @Test void convierteUnaPeticionDeInscripcionDTOAUnaPeticionDeInscripcion() {
    PeticionInscriptionDTO peticionDTO = new PeticionInscriptionDTO();
    peticionDTO.setNombre("Ana Garcia");
    peticionDTO.setLegajo("54321");
    peticionDTO.setHistoriaAcademica(new HistoriaAcademicaDTO());
    peticionDTO.setCorrelativa(false);

    PeticionInscripcion peticion = mapper.toPeticionInscripcion(peticionDTO);

    assertEquals("Ana Garcia", peticion.getEstudiante().getNombre());
    assertEquals("54321", peticion.getEstudiante().getLegajo());
    assertFalse(peticion.isCumpleCorrelativa());
  }

  @Test void convierteUnaListaDePeticionesDeInscriptionDTOAListaDePeticionesDeInscripcion() {
    PeticionInscriptionDTO peticionDTO = new PeticionInscriptionDTO();
    peticionDTO.setNombre("Juan Perez");
    peticionDTO.setLegajo("12345");
    peticionDTO.setHistoriaAcademica(new HistoriaAcademicaDTO());
    peticionDTO.setCorrelativa(true);
    List<PeticionInscriptionDTO> peticionesDTO = List.of(peticionDTO);

    List<PeticionInscripcion> peticiones = mapper.toPeticionInscripcionList(peticionesDTO);

    assertThat(peticiones).hasSize(1);
    PeticionInscripcion peticion = peticiones.get(0);
    assertEquals("Juan Perez", peticion.getEstudiante().getNombre());
    assertEquals("12345", peticion.getEstudiante().getLegajo());
    assertTrue(peticion.isCumpleCorrelativa());
  }

  @Test void convierteUnaPeticionInscripcionCsvDTOAUnaPeticionInscripcion() {
    PeticionInscripcionCsvDTO csvDto = new PeticionInscripcionCsvDTO();
    csvDto.setLegajo("22222");
    csvDto.setCodigoComision("COM02");

    Estudiante estudiante = Estudiante.builder().legajo("22222").nombre("Laura").build();
    Materia materia = Materia.builder().codigo("MAT02").nombre("Fisica").build();
    Comision comision = Comision.builder().codigo("COM02").materia(materia).build();

    when(mappingService.buscarEstudiantePorLegajo("22222")).thenReturn(estudiante);
    when(mappingService.buscarComisionPorCodigo("COM02")).thenReturn(comision);


    PeticionInscripcion peticion = mapper.toPeticionInscripcion(csvDto, mappingService);


    assertEquals("22222", peticion.getEstudiante().getLegajo());
    assertEquals("MAT02", peticion.getMateria());
    assertEquals(1, peticion.getComisiones().size());
    assertEquals("COM02", peticion.getComisiones().get(0).getCodigo());
    assertFalse(peticion.isCumpleCorrelativa());
  }

  @Test void convierteListaDePeticionInscripcionCsvDTOAPeticionesDeInscripcion() {
    PeticionInscripcionCsvDTO csvDTO = new PeticionInscripcionCsvDTO();
    csvDTO.setLegajo("11111");
    csvDTO.setCodigoComision("COM01");

    Estudiante estudiante = Estudiante.builder().legajo("11111").nombre("Pepe").build();
    Materia materia = Materia.builder().codigo("MAT01").nombre("Matematica").build();
    Comision comision = Comision.builder().codigo("COM01").materia(materia).build();

    when(mappingService.buscarEstudiantePorLegajo("11111")).thenReturn(estudiante);
    when(mappingService.buscarComisionPorCodigo("COM01")).thenReturn(comision);


    List<PeticionInscripcion> peticiones = mapper.toPeticionInscripcionCsvList(List.of(csvDTO),
      mappingService);


    assertThat(peticiones).hasSize(1);
    PeticionInscripcion peticion = peticiones.get(0);
    assertEquals("11111", peticion.getEstudiante().getLegajo());
    assertEquals("MAT01", peticion.getMateria());
    assertEquals(1, peticion.getComisiones().size());
    assertEquals("COM01", peticion.getComisiones().get(0).getCodigo());
    assertFalse(peticion.isCumpleCorrelativa());
  }

  @Test void conviertePeticionInscripcionAJson() {

    Estudiante estudiante = Estudiante.builder().nombre("Tomas").legajo("7890").build();
    PeticionInscripcion peticion = PeticionInscripcion.builder().materia("MAT01")
                                                      .cumpleCorrelativa(true)
                                                      .estudiante(estudiante).build();

    String json = mapper.toJson(peticion);

    assertThat(json).contains("Tomas");
    assertThat(json).contains("7890");
    assertThat(json).contains("MAT01");
  }

  @Test void lanzaUnErrorSiFallaLaSerializacionAJson() {
    PeticionInscripcion peticion = mock(PeticionInscripcion.class);
    when(peticion.getMateria()).thenThrow(new RuntimeException("Fallo interno"));

    RuntimeException ex = assertThrows(RuntimeException.class, () -> mapper.toJson(peticion));
    assertThat(ex.getMessage()).contains("Error al convertir PeticionInscripcion a JSON");
  }
}
