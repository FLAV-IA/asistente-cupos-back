package com.edu.asistente_cupos.assembler;

import com.edu.asistente_cupos.controller.dto.PeticionInscripcionCsvDTO;
import com.edu.asistente_cupos.domain.Comision;
import com.edu.asistente_cupos.domain.Estudiante;
import com.edu.asistente_cupos.domain.peticion.PeticionInscripcion;
import com.edu.asistente_cupos.domain.peticion.PeticionPorMateria;
import com.edu.asistente_cupos.excepcion.EstudianteNoEncontradoException;
import com.edu.asistente_cupos.repository.ComisionRepository;
import com.edu.asistente_cupos.repository.EstudianteRepository;
import com.edu.asistente_cupos.service.factory.PeticionDeMateriaFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyMap;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EnsambladorDePeticionesTest {

  @Mock
  private EstudianteRepository estudianteRepository;

  @Mock
  private ComisionRepository comisionRepository;

  @Mock
  private PeticionDeMateriaFactory peticionDeMateriaFactory;

  @InjectMocks
  private EnsambladorDePeticiones ensamblador;

  @Test
  void ensamblarDesdeCsvDtoDebeEnsamblarCorrectamente() {
    PeticionInscripcionCsvDTO dto = new PeticionInscripcionCsvDTO();
    dto.setDni("123");
    dto.setCodigosComisiones("COM1,COM2");

    Estudiante estudiante = Estudiante.builder().dni("123").nombre("Juan").build();
    Comision com1 = new Comision("COM1", "lunes", 30, null);
    Comision com2 = new Comision("COM2", "martes", 30, null);

    PeticionPorMateria peticion = PeticionPorMateria.builder().comisiones(List.of(com1, com2))
                                                    .build();

    when(estudianteRepository.findByDniIn(Set.of("123"))).thenReturn(List.of(estudiante));
    when(comisionRepository.findByCodigoIn(Set.of("COM1", "COM2"))).thenReturn(List.of(com1, com2));
    when(peticionDeMateriaFactory.crearPeticionDeMateria(eq(dto), anyMap(),
      eq(estudiante))).thenReturn(peticion);


    List<PeticionInscripcion> resultado = ensamblador.ensamblarDesdeCsvDto(List.of(dto));


    assertEquals(1, resultado.size());
    assertEquals("123", resultado.get(0).getEstudiante().getDni());
    assertEquals(1, resultado.get(0).getPeticionPorMaterias().size());
  }

  @Test
  void ensamblarDesdeCsvDtoFallaSiEstudianteNoExiste() {
    PeticionInscripcionCsvDTO dto = new PeticionInscripcionCsvDTO();
    dto.setDni("999");
    dto.setCodigosComisiones("COM1");
    when(estudianteRepository.findByDniIn(Set.of("999"))).thenReturn(List.of());

    assertThrows(EstudianteNoEncontradoException.class,
      () -> ensamblador.ensamblarDesdeCsvDto(List.of(dto)));
  }

  @Test
  void ensamblarDesdeCsvDtoIgnoraCodigosDeComisionVacios() {
    PeticionInscripcionCsvDTO dto = new PeticionInscripcionCsvDTO();
    dto.setDni("123");
    dto.setCodigosComisiones(" , , ");

    Estudiante estudiante = Estudiante.builder().dni("123").nombre("Ana").build();

    when(estudianteRepository.findByDniIn(Set.of("123"))).thenReturn(List.of(estudiante));
    when(comisionRepository.findByCodigoIn(Set.of())).thenReturn(List.of());

    when(peticionDeMateriaFactory.crearPeticionDeMateria(any(), any(), any())).thenReturn(null);


    List<PeticionInscripcion> resultado = ensamblador.ensamblarDesdeCsvDto(List.of(dto));


    assertEquals(1, resultado.size());
    assertTrue(resultado.get(0).getPeticionPorMaterias().isEmpty());
  }
}
