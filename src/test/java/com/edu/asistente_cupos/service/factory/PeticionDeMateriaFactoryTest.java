package com.edu.asistente_cupos.service.factory;

import com.edu.asistente_cupos.controller.dto.PeticionInscripcionCsvDTO;
import com.edu.asistente_cupos.domain.Comision;
import com.edu.asistente_cupos.domain.Estudiante;
import com.edu.asistente_cupos.domain.Materia;
import com.edu.asistente_cupos.domain.peticion.PeticionPorMateria;
import com.edu.asistente_cupos.excepcion.ComisionesDeDistintaMateriaException;
import com.edu.asistente_cupos.excepcion.NoSeEspecificaronComisionesException;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PeticionDeMateriaFactoryTest {
  PeticionDeMateriaFactory factory = new PeticionDeMateriaFactory();

  @Test
  void creaPeticionCorrectamenteSiTodoEsValido() {
    PeticionInscripcionCsvDTO dto = new PeticionInscripcionCsvDTO();
    dto.setCodigosComisiones("COM1, COM2");

    Materia materia = Materia.builder().codigo("MAT1").nombre("Matemática").build();

    Comision c1 = new Comision("COM1", "lunes", 30, materia);
    Comision c2 = new Comision("COM2", "miércoles", 30, materia);

    Estudiante estudiante = mock(Estudiante.class);
    when(estudiante.puedeInscribirse(materia)).thenReturn(true);

    Map<String, Comision> mapa = Map.of("COM1", c1, "COM2", c2);

    PeticionPorMateria peticion = factory.crearPeticionDeMateria(dto, mapa, estudiante);

    assertThat(peticion.getComisiones()).containsExactly(c1, c2);
    assertThat(peticion.isCumpleCorrelativa()).isTrue();
  }

  @Test
  void lanzaExcepcionSiFaltaUnaComision() {
    PeticionInscripcionCsvDTO dto = new PeticionInscripcionCsvDTO();
    dto.setCodigosComisiones("COM1, COM2");

    Comision c1 = new Comision("COM1", "martes", 30,
      Materia.builder().codigo("MAT1").nombre("X").build());

    Map<String, Comision> mapa = Map.of("COM1", c1);
    Estudiante estudiante = mock(Estudiante.class);

    assertThrows(ComisionesDeDistintaMateriaException.class,
      () -> factory.crearPeticionDeMateria(dto, mapa, estudiante));
  }

  @Test
  void lanzaExcepcionSiNoHayComisiones() {
    PeticionInscripcionCsvDTO dto = new PeticionInscripcionCsvDTO();
    dto.setCodigosComisiones("");

    Estudiante estudiante = mock(Estudiante.class);
    Map<String, Comision> mapa = Map.of();

    assertThrows(NoSeEspecificaronComisionesException.class,
      () -> factory.crearPeticionDeMateria(dto, mapa, estudiante));
  }

  @Test
  void lanzaExcepcionSiLasComisionesSonDeMateriasDiferentes() {
    PeticionInscripcionCsvDTO dto = new PeticionInscripcionCsvDTO();
    dto.setCodigosComisiones("COM1, COM2");

    Comision c1 = new Comision("COM1", "lunes", 30,
      Materia.builder().codigo("MAT1").nombre("Mate").build());
    Comision c2 = new Comision("COM2", "miércoles", 30,
      Materia.builder().codigo("MAT2").nombre("Física").build());

    Map<String, Comision> mapa = Map.of("COM1", c1, "COM2", c2);

    Estudiante estudiante = mock(Estudiante.class);

    assertThrows(ComisionesDeDistintaMateriaException.class,
      () -> factory.crearPeticionDeMateria(dto, mapa, estudiante));
  }
}
