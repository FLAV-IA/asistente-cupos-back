package com.edu.asistente_cupos.domain.cursada;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class EstadoCursadaDesaprobadaTest {
  @Test
  void estadoDesaprobadoDevuelveFalseYNotaCorrecta() {
    EstadoCursada estado = new EstadoCursadaDesaprobada(4);

    assertFalse(estado.fueAprobada());
    assertFalse(estado.estaEnCurso());
    assertEquals(4, estado.getNota());
  }
}
