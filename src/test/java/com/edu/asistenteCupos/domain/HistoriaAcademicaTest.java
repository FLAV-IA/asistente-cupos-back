package com.edu.asistenteCupos.domain;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class HistoriaAcademicaTest {
  @Test
  void devuelveTrueSiCumpleCorrelativa() {
    Materia materia = Materia.builder().codigo("MAT1").nombre("Matemática I").build();

    HistoriaAcademica historia = HistoriaAcademica.builder().cursadasAnteriores(List.of())
                                                  .inscripcionesActuales(Set.of()).build();

    boolean resultado = historia.cumpleCorrelativa(materia);

    assertThat(resultado).isTrue();
  }
}