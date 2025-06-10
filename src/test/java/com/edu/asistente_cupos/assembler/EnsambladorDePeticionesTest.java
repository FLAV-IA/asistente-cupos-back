package com.edu.asistente_cupos.assembler;

import com.edu.asistente_cupos.controller.dto.PeticionInscripcionCsvDTO;
import com.edu.asistente_cupos.controller.dto.PeticionInscripcionDTO;
import com.edu.asistente_cupos.controller.dto.PeticionPorMateriaDTO;
import com.edu.asistente_cupos.domain.Comision;
import com.edu.asistente_cupos.domain.Estudiante;
import com.edu.asistente_cupos.domain.horario.HorarioParser;
import com.edu.asistente_cupos.domain.peticion.PeticionInscripcion;
import com.edu.asistente_cupos.domain.peticion.PeticionPorMateria;
import com.edu.asistente_cupos.excepcion.EstudianteNoEncontradoException;
import com.edu.asistente_cupos.repository.ComisionRepository;
import com.edu.asistente_cupos.repository.EstudianteRepository;
import com.edu.asistente_cupos.repository.MateriaRepository;
import com.edu.asistente_cupos.service.factory.PeticionInscripcionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyMap;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EnsambladorDePeticionesTest {

  @Mock
  EstudianteRepository estudianteRepository;
  @Mock
  ComisionRepository comisionRepository;
  @Mock
  MateriaRepository materiaRepository;
  @Mock
  PeticionInscripcionFactory peticionInscripcionFactory;

  @InjectMocks
  EnsambladorDePeticiones ensamblador;

  @Test
  void ensamblarDesdeCsvDtoDebeEnsamblarCorrectamente() {
    PeticionInscripcionCsvDTO dto = new PeticionInscripcionCsvDTO();
    dto.setDni("123");
    dto.setCodigosComisiones("COM1,COM2");

    Estudiante estudiante = Estudiante.builder().dni("123").nombre("Juan").build();
    Comision com1 = new Comision("COM1", HorarioParser.parse("LUNES 09:00 a 10:00"), 30, null,new ArrayList<>());
    Comision com2 = new Comision("COM2", HorarioParser.parse("MARTES 09:00 a 10:00"), 30, null,new ArrayList<>());

    PeticionPorMateria peticion = PeticionPorMateria.builder().comisiones(List.of(com1, com2))
                                                    .build();
    PeticionInscripcion peticionInscripcion = PeticionInscripcion.builder().estudiante(estudiante)
                                                                 .peticionPorMaterias(
                                                                   List.of(peticion)).build();

    when(estudianteRepository.findByDniIn(Set.of("123"))).thenReturn(List.of(estudiante));
    when(comisionRepository.findByCodigoIn(Set.of("COM1", "COM2"))).thenReturn(List.of(com1, com2));
    when(peticionInscripcionFactory.crearDesdeCsv(eq(dto), eq(estudiante), anyMap())).thenReturn(
      peticionInscripcion);

    List<PeticionInscripcion> resultado = ensamblador.ensamblarDesdeCsvDto(List.of(dto));

    assertEquals(1, resultado.size());
    assertEquals("123", resultado.get(0).getEstudiante().getDni());
    assertEquals("COM1",
      resultado.get(0).getPeticionPorMaterias().get(0).getComisiones().get(0).getCodigo());
  }

  @Test
  void ensamblarDesdeCsvDtoFallaSiEstudianteNoExiste() {
    PeticionInscripcionCsvDTO dto = new PeticionInscripcionCsvDTO();
    dto.setDni("999");
    dto.setCodigosComisiones("COM1");

    when(estudianteRepository.findByDniIn(Set.of("999"))).thenReturn(List.of());

    EstudianteNoEncontradoException exception = assertThrows(EstudianteNoEncontradoException.class,
      () -> ensamblador.ensamblarDesdeCsvDto(List.of(dto)));

    assertTrue(exception.getMessage().contains("999"));
  }

  @Test
  void ensamblarDesdeCsvDtoIgnoraCodigosDeComisionVacios() {
    PeticionInscripcionCsvDTO dto = new PeticionInscripcionCsvDTO();
    dto.setDni("123");
    dto.setCodigosComisiones(" , , ");

    Estudiante estudiante = Estudiante.builder().dni("123").nombre("Ana").build();
    PeticionInscripcion peticion = PeticionInscripcion.builder().estudiante(estudiante)
                                                      .peticionPorMaterias(emptyList()).build();

    when(estudianteRepository.findByDniIn(Set.of("123"))).thenReturn(List.of(estudiante));
    when(comisionRepository.findByCodigoIn(Set.of())).thenReturn(List.of());
    when(peticionInscripcionFactory.crearDesdeCsv(eq(dto), eq(estudiante), anyMap())).thenReturn(
      peticion);

    List<PeticionInscripcion> resultado = ensamblador.ensamblarDesdeCsvDto(List.of(dto));

    assertEquals(1, resultado.size());
    assertTrue(resultado.get(0).getPeticionPorMaterias().isEmpty());
  }

  @Test
  void ensamblarDesdeCsvDtoConComisionesDuplicadasLasProcesaUnaVez() {
    PeticionInscripcionCsvDTO dto = new PeticionInscripcionCsvDTO();
    dto.setDni("888");
    dto.setCodigosComisiones("COM1, COM1, COM1");

    Estudiante estudiante = Estudiante.builder().dni("888").nombre("Luis").build();
    Comision com1 = new Comision("COM1", HorarioParser.parse("LUNES 09:00 a 10:00"), 30, null);

    PeticionPorMateria peticion = PeticionPorMateria.builder().comisiones(List.of(com1)).build();
    PeticionInscripcion peticionInscripcion = PeticionInscripcion.builder().estudiante(estudiante)
                                                                 .peticionPorMaterias(
                                                                   List.of(peticion)).build();

    when(estudianteRepository.findByDniIn(Set.of("888"))).thenReturn(List.of(estudiante));
    when(comisionRepository.findByCodigoIn(Set.of("COM1"))).thenReturn(List.of(com1));
    when(peticionInscripcionFactory.crearDesdeCsv(eq(dto), eq(estudiante), anyMap())).thenReturn(
      peticionInscripcion);

    List<PeticionInscripcion> resultado = ensamblador.ensamblarDesdeCsvDto(List.of(dto));

    assertEquals(1, resultado.size());
    assertEquals("COM1",
      resultado.get(0).getPeticionPorMaterias().get(0).getComisiones().get(0).getCodigo());
  }

  @Test
  void ensamblarDesdeCsvDtoFiltraNullsDeFactory() {
    PeticionInscripcionCsvDTO dto = new PeticionInscripcionCsvDTO();
    dto.setDni("111");
    dto.setCodigosComisiones("COMX");

    Estudiante estudiante = Estudiante.builder().dni("111").nombre("Tomás").build();

    when(estudianteRepository.findByDniIn(Set.of("111"))).thenReturn(List.of(estudiante));
    when(comisionRepository.findByCodigoIn(Set.of("COMX"))).thenReturn(List.of());
    when(peticionInscripcionFactory.crearDesdeCsv(eq(dto), eq(estudiante), anyMap())).thenReturn(
      null);

    List<PeticionInscripcion> resultado = ensamblador.ensamblarDesdeCsvDto(List.of(dto));

    assertTrue(resultado.isEmpty());
  }

  @Test
  void ensamblarDesdeDtoDebeEnsamblarCorrectamente() {
    PeticionPorMateriaDTO porMateria = new PeticionPorMateriaDTO("Álgebra", "MAT1",
      List.of("COM1", "COM2"), true);
    PeticionInscripcionDTO dto = new PeticionInscripcionDTO("Juan", "123", null,
      List.of(porMateria));

    Estudiante estudiante = Estudiante.builder().dni("123").nombre("Juan").build();
    Comision com1 = new Comision("COM1", HorarioParser.parse("LUNES 08:00 a 10:00"), 25, null);
    Comision com2 = new Comision("COM2", HorarioParser.parse("MARTES 08:00 a 10:00"), 25, null);

    PeticionPorMateria peticion = PeticionPorMateria.builder().comisiones(List.of(com1, com2))
                                                    .build();
    PeticionInscripcion peticionInscripcion = PeticionInscripcion.builder().estudiante(estudiante)
                                                                 .peticionPorMaterias(
                                                                   List.of(peticion)).build();

    when(estudianteRepository.findByDniIn(Set.of("123"))).thenReturn(List.of(estudiante));
    when(comisionRepository.findByCodigoIn(Set.of("COM1", "COM2"))).thenReturn(List.of(com1, com2));
    when(peticionInscripcionFactory.crearDesdeDto(eq(dto), eq(estudiante), anyMap(),
      eq(materiaRepository))).thenReturn(peticionInscripcion);

    List<PeticionInscripcion> resultado = ensamblador.ensamblarDesdeDto(List.of(dto));

    assertEquals(1, resultado.size());
    assertEquals("123", resultado.get(0).getEstudiante().getDni());
    assertEquals(2, resultado.get(0).getPeticionPorMaterias().get(0).getComisiones().size());
  }

  @Test
  void ensamblarDesdeDtoFallaSiEstudianteNoExiste() {
    PeticionPorMateriaDTO porMateria = new PeticionPorMateriaDTO("Redes", "MAT9", List.of("COM5"),
      false);
    PeticionInscripcionDTO dto = new PeticionInscripcionDTO("Luis", "456", null,
      List.of(porMateria));

    when(estudianteRepository.findByDniIn(Set.of("456"))).thenReturn(List.of());

    EstudianteNoEncontradoException ex = assertThrows(EstudianteNoEncontradoException.class,
      () -> ensamblador.ensamblarDesdeDto(List.of(dto)));

    assertTrue(ex.getMessage().contains("456"));
  }
}
