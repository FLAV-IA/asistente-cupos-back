package com.edu.asistente_cupos.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidadorHorarioTest {
  @Test
  void devuelveTrueSiHaySuperposicionEnElMismoDia() {
    String h1 = "LUNES 09:00 a 10:00";
    String h2 = "LUNES 09:30 a 11:00";
    assertTrue(ValidadorHorario.haySuperposicion(h1, h2));
  }

  @Test
  void devuelveFalseSiSonDiasDistintos() {
    String h1 = "LUNES 09:00 a 10:00";
    String h2 = "MARTES 09:00 a 10:00";
    assertFalse(ValidadorHorario.haySuperposicion(h1, h2));
  }

  @Test
  void devuelveFalseSiUnHorarioTerminaAntesDeQueEmpieceElOtro() {
    String h1 = "MIERCOLES 08:00 a 09:00";
    String h2 = "MIERCOLES 09:01 a 10:00";
    assertFalse(ValidadorHorario.haySuperposicion(h1, h2));
  }

  @Test
  void devuelveTrueSiUnHorarioTerminaExactamenteCuandoEmpiezaElOtro() {
    String h1 = "JUEVES 08:00 a 09:00";
    String h2 = "JUEVES 09:00 a 10:00";
    assertTrue(ValidadorHorario.haySuperposicion(h1, h2));
  }

  @Test
  void devuelveTrueSiHaySuperposicionEnBloquesMultiples() {
    String h1 = "LUNES 08:00 a 09:00, MARTES 10:00 a 12:00";
    String h2 = "MARTES 11:00 a 13:00";
    assertTrue(ValidadorHorario.haySuperposicion(h1, h2));
  }

  @Test
  void devuelveFalseSiNoHaySuperposicionEnNingunBloque() {
    String h1 = "LUNES 08:00 a 09:00, MIERCOLES 10:00 a 12:00";
    String h2 = "MARTES 09:00 a 10:00, JUEVES 11:00 a 12:00";
    assertFalse(ValidadorHorario.haySuperposicion(h1, h2));
  }

  @Test
  void soportaHorariosVaciosSinExplotar() {
    assertFalse(ValidadorHorario.haySuperposicion("", "LUNES 10:00 a 11:00"));
    assertFalse(ValidadorHorario.haySuperposicion("LUNES 10:00 a 11:00", ""));
    assertFalse(ValidadorHorario.haySuperposicion("", ""));
  }

  @Test
  void soportaHorariosNulosSinExplotar() {
    assertFalse(ValidadorHorario.haySuperposicion(null, "LUNES 10:00 a 11:00"));
    assertFalse(ValidadorHorario.haySuperposicion("LUNES 10:00 a 11:00", null));
    assertFalse(ValidadorHorario.haySuperposicion(null, null));
  }
}
