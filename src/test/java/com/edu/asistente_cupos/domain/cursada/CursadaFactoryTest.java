package com.edu.asistente_cupos.domain.cursada;

import com.edu.asistente_cupos.domain.Materia;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CursadaFactoryTest {
  @Test
  void creaCursadaAprobadaConNotaCorrecta() {
    Materia materia = Materia.builder().codigo("MAT101").nombre("Matemática I").build();
    Cursada cursada = CursadaFactory.aprobada(materia, 8);

    assertTrue(cursada.getEstado().fueAprobada());
    assertFalse(cursada.getEstado().estaEnCurso());
    assertEquals(8, cursada.getEstado().getNota());
    assertEquals(materia, cursada.getMateria());
  }

  @Test
  void creaCursadaDesaprobadaConNotaCorrecta() {
    Materia materia = Materia.builder().codigo("MAT102").nombre("Álgebra").build();
    Cursada cursada = CursadaFactory.desaprobada(materia, 3);

    assertFalse(cursada.getEstado().fueAprobada());
    assertFalse(cursada.getEstado().estaEnCurso());
    assertEquals(3, cursada.getEstado().getNota());
  }

  @Test
  void creaCursadaEnCursoSinNota() {
    Materia materia = Materia.builder().codigo("MAT103").nombre("Análisis").build();
    Cursada cursada = CursadaFactory.enCurso(materia);

    assertFalse(cursada.getEstado().fueAprobada());
    assertTrue(cursada.getEstado().estaEnCurso());
    assertThrows(IllegalStateException.class, () -> cursada.getEstado().getNota());
  }
}
