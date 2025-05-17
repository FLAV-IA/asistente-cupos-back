package com.edu.asistenteCupos.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EstudianteTest {
  @Test
  void puedeInscribirseRetornaTrueSiCumpleCorrelativa() {
    Materia materia = Materia.builder().codigo("MAT1").nombre("Matemática I").build();

    HistoriaAcademica historia = HistoriaAcademica.builder().totalInscripcionesHistoricas(5)
                                                  .totalHistoricasAprobadas(4).coeficiente(8.5)
                                                  .build();

    Estudiante estudiante = Estudiante.builder().dni("12345678").nombre("Juan").legajo("LEG123")
                                      .mail("juan@example.com").build();

    estudiante.setHistoriaAcademica(historia);

    assertThat(estudiante.puedeInscribirse(materia)).isTrue();
  }
}