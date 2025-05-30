package com.edu.asistente_cupos.domain.cursada;

import com.edu.asistente_cupos.domain.Materia;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CursadaTest {
  @Test
  void cursadaConEstadoAprobadoDevuelveTrueEnFueAprobada() {
    Materia materia = Materia.builder().codigo("MAT201").nombre("Probabilidad").build();
    EstadoCursada estado = new EstadoCursadaAprobada(10);
    Cursada cursada = Cursada.builder().materia(materia).estado(estado).build();

    assertTrue(cursada.fueAprobada());
    assertEquals(materia, cursada.getMateria());
    assertEquals(10, cursada.getEstado().getNota());
  }

  @Test
  void cursadaConEstadoDesaprobadoDevuelveFalseEnFueAprobada() {
    Materia materia = Materia.builder().codigo("MAT202").nombre("Estadística").build();
    EstadoCursada estado = new EstadoCursadaDesaprobada(3);
    Cursada cursada = Cursada.builder().materia(materia).estado(estado).build();

    assertFalse(cursada.fueAprobada());
    assertEquals(3, cursada.getEstado().getNota());
  }

  @Test
  void cursadaConEstadoEnCursoDevuelveFalseEnFueAprobada() {
    Materia materia = Materia.builder().codigo("MAT203").nombre("Geometría").build();
    EstadoCursada estado = new EstadoCursadaEnCurso();
    Cursada cursada = Cursada.builder().materia(materia).estado(estado).build();

    assertFalse(cursada.fueAprobada());
    assertTrue(cursada.getEstado().estaEnCurso());
    assertThrows(IllegalStateException.class, () -> cursada.getEstado().getNota());
  }
}
