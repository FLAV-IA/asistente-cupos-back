package com.edu.asistente_cupos.domain.cursada;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EstadoCursadaAprobadaTest {
  @Test
  void estadoAprobadaDevuelveTrueYNotaCorrecta() {
    EstadoCursada estado = new EstadoCursadaAprobada(9);

    assertTrue(estado.fueAprobada());
    assertFalse(estado.estaEnCurso());
    assertEquals(9, estado.getNota());
  }
}
