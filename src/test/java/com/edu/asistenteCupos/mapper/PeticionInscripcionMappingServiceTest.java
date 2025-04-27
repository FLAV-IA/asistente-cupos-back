package com.edu.asistenteCupos.mapper;

import com.edu.asistenteCupos.domain.Comision;
import com.edu.asistenteCupos.domain.Estudiante;
import com.edu.asistenteCupos.repository.ComisionRepository;
import com.edu.asistenteCupos.repository.EstudianteRepository;
import com.edu.asistenteCupos.repository.MateriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PeticionInscripcionMappingServiceTest {
  private EstudianteRepository estudianteRepository;
  private ComisionRepository comisionRepository;
  private MateriaRepository materiaRepository;
  private PeticionInscripcionMappingService mappingService;

  @BeforeEach
  void setUp() {
    estudianteRepository = mock(EstudianteRepository.class);
    comisionRepository = mock(ComisionRepository.class);
    materiaRepository = mock(MateriaRepository.class);
    mappingService = new PeticionInscripcionMappingService(estudianteRepository, comisionRepository,
      materiaRepository);
  }

  @Test
  void buscarEstudiantePorDniDaAlEstudianteBuscado() {
    Estudiante estudiante = Estudiante.builder().dni("12345").nombre("Juan Perez").build();
    when(estudianteRepository.findByCodigo("12345")).thenReturn(Optional.of(estudiante));

    Estudiante resultadoBuscado = mappingService.buscarEstudiantePorDni("12345");

    assertEquals("12345", resultadoBuscado.getDni());
    assertEquals("Juan Perez", resultadoBuscado.getNombre());
  }

  @Test
  void buscarUnEstudianteQueNoExisteDaUnError() {
    when(estudianteRepository.findByCodigo("99999")).thenReturn(Optional.empty());

    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
      () -> mappingService.buscarEstudiantePorDni("99999"));
    assertEquals("No se encontró Estudiante con dni: 99999", ex.getMessage());
  }

  @Test
  void buscarUnaComisionPorCodigoDaLaComisionBuscada() {
    Comision comision = Comision.builder().codigo("COM01").horario("Lunes 10hs").build();
    when(comisionRepository.findById("COM01")).thenReturn(Optional.of(comision));

    Comision comisionBuscada = mappingService.buscarComisionPorCodigo("COM01");

    assertEquals("COM01", comisionBuscada.getCodigo());
    assertEquals("Lunes 10hs", comisionBuscada.getHorario());
  }

  @Test
  void buscarUnaComisionQueNoExisteDaUnError() {
    when(comisionRepository.findById("COM99")).thenReturn(Optional.empty());

    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
      () -> mappingService.buscarComisionPorCodigo("COM99"));
    assertEquals("No se encontró Comisión con código: COM99", ex.getMessage());
  }
}
