package com.edu.asistente_cupos.domain;

import com.edu.asistente_cupos.domain.horario.HorarioParser;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ComisionTest {
  @Test
  void creaComisionConBuilder() {
    Materia materia = Materia.builder().codigo("MAT101").nombre("Matemática I").build();

    Comision comision = Comision.builder().codigo("C1")
                                .horario(HorarioParser.parse("LUNES 10:30 a 12:30")).cupo(30)
                                .materia(materia).build();

    assertEquals("C1", comision.getCodigo());
    assertEquals("LUNES 10:30 a 12:30", comision.getHorario().toString());
    assertEquals(30, comision.getCupo());
    assertEquals(materia, comision.getMateria());
  }

  @Test
  void comisionesConMismoCodigoSonIguales() {
    Comision c1 = Comision.builder().codigo("C1").build();
    Comision c2 = Comision.builder().codigo("C1").build();

    assertEquals(c1, c2);
    assertEquals(c1.hashCode(), c2.hashCode());
  }

  @Test
  void comisionesConDistintoCodigoNoSonIguales() {
    Comision c1 = Comision.builder().codigo("C1").build();
    Comision c2 = Comision.builder().codigo("C2").build();

    assertNotEquals(c1, c2);
    assertNotEquals(c1.hashCode(), c2.hashCode());
  }

  @Test
  void equalsDebeSerFalseConObjetoDistintoONull() {
    Comision c1 = Comision.builder().codigo("C1").build();

    assertNotEquals(null, c1);
    assertNotEquals("otro tipo", c1);
  }

  @Test
  void tieneCupoDevuelveTrueSiCupoMayorACero() {
    Comision c = Comision.builder().codigo("C1").cupo(10).asignaciones(new ArrayList<>()).build();
    assertTrue(c.tieneCupo());
  }

  @Test
  void tieneCupoDevuelveFalseSiCupoEsCero() {
    Comision c = Comision.builder().codigo("C1").cupo(0).asignaciones(new ArrayList<>()).build();
    assertFalse(c.tieneCupo());
  }

  @Test
  void tieneCupoDevuelveFalseSiCupoEsNegativo() {
    Comision c = Comision.builder().codigo("C1").cupo(-5).asignaciones(new ArrayList<>()).build();
    assertFalse(c.tieneCupo());
  }

  @Test
  void materiaPuedeSerAsignadaYRecuperada() {
    Materia m = Materia.builder().codigo("MAT202").nombre("Álgebra").build();
    Comision c = Comision.builder().codigo("C1").materia(m).build();
    assertEquals("MAT202", c.getMateria().getCodigo());
    assertEquals("Álgebra", c.getMateria().getNombre());
  }
}