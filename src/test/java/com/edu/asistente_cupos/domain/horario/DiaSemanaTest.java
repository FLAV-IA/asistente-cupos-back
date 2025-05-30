package com.edu.asistente_cupos.domain.horario;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DiaSemanaTest {
  @Test
  void convierteNombreValidoAEnumSinImportarCase() {
    assertEquals(DiaSemana.LUNES, DiaSemana.desdeTexto("lunes"));
    assertEquals(DiaSemana.MARTES, DiaSemana.desdeTexto("MARTES"));
    assertEquals(DiaSemana.MIERCOLES, DiaSemana.desdeTexto("MiErCoLeS"));
  }

  @Test
  void ignoraEspaciosAntesYDespues() {
    assertEquals(DiaSemana.JUEVES, DiaSemana.desdeTexto("  jueves  "));
    assertEquals(DiaSemana.DOMINGO, DiaSemana.desdeTexto("\tDomingo\n"));
  }

  @Test
  void lanzaExcepcionSiNoCoincideConEnum() {
    assertThrows(IllegalArgumentException.class, () -> DiaSemana.desdeTexto("lun"));
    assertThrows(IllegalArgumentException.class, () -> DiaSemana.desdeTexto("abc"));
    assertThrows(IllegalArgumentException.class, () -> DiaSemana.desdeTexto("viernesito"));
  }

  @Test
  void lanzaExcepcionSiInputEsNull() {
    assertThrows(NullPointerException.class, () -> DiaSemana.desdeTexto(null));
  }
}
