package com.edu.asistente_cupos.domain.cursada;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EstadoCursadaEnCursoTest {
  @Test
  void estadoEnCursoDevuelveTrueSoloEnCurso() {
    EstadoCursada estado = new EstadoCursadaEnCurso();

    assertFalse(estado.fueAprobada());
    assertTrue(estado.estaEnCurso());
  }

  @Test
  void getNotaLanzaError() {
    EstadoCursada estado = new EstadoCursadaEnCurso();

    IllegalStateException exception = assertThrows(IllegalStateException.class, estado::getNota);
    assertEquals("La cursada en curso no tiene nota.", exception.getMessage());
  }
}
