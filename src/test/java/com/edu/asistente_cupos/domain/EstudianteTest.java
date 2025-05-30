package com.edu.asistente_cupos.domain;

import com.edu.asistente_cupos.domain.cursada.Cursada;
import com.edu.asistente_cupos.domain.cursada.CursadaFactory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class EstudianteTest {
  @Test
  void puedeInscribirseRetornaTrueSiCumpleCorrelativa() {
    Materia materia = Materia.builder().codigo("MAT1").nombre("Matemática I").build();

    HistoriaAcademica historia = HistoriaAcademica.builder().coeficiente(8.5).build();
    Estudiante estudiante = Estudiante.builder().dni("12345678").nombre("Juan").legajo("LEG123")
                                      .mail("juan@example.com").build();

    estudiante.setHistoriaAcademica(historia);

    assertThat(estudiante.puedeInscribirse(materia)).isTrue();
  }

  @Test
  void puedeInscribirseRetornaFalseSiNoCumpleCorrelativa() {
    Materia correlativa = Materia.builder().codigo("MAT2").build();
    Materia materia = Materia.builder().codigo("MAT3").correlativas(List.of(correlativa)).build();

    HistoriaAcademica historia = HistoriaAcademica.builder().cursadas(List.of()).build();
    Estudiante estudiante = Estudiante.builder().dni("87654321").build();
    estudiante.setHistoriaAcademica(historia);

    assertThat(estudiante.puedeInscribirse(materia)).isFalse();
  }

  @Test
  void estaInscriptoEnMasDeRetornaTrueSiSuperaCantidad() {
    Materia matA = Materia.builder().codigo("MAT4").build();
    Materia matB = Materia.builder().codigo("MAT5").build();
    Cursada cursada1 = CursadaFactory.enCurso(matA);
    Cursada cursada2 = CursadaFactory.enCurso(matB);

    HistoriaAcademica historia = HistoriaAcademica.builder().cursadas(List.of(cursada1, cursada2))
                                                  .build();
    Estudiante estudiante = Estudiante.builder().dni("555").build();
    estudiante.setHistoriaAcademica(historia);

    assertThat(estudiante.estaInscriptoEnMasDe(1)).isTrue();
  }

  @Test
  void estaInscriptoEnMasDeRetornaFalseSiNoSuperaCantidad() {
    Materia matA = Materia.builder().codigo("MAT6").build();
    Cursada cursada = CursadaFactory.enCurso(matA);

    HistoriaAcademica historia = HistoriaAcademica.builder().cursadas(List.of(cursada)).build();
    Estudiante estudiante = Estudiante.builder().dni("444").build();
    estudiante.setHistoriaAcademica(historia);

    assertThat(estudiante.estaInscriptoEnMasDe(1)).isFalse();
  }

  @Test
  void setHistoriaAcademicaEstableceRelacionBidireccional() {
    Estudiante estudiante = Estudiante.builder().dni("999").build();
    HistoriaAcademica historia = HistoriaAcademica.builder().build();

    estudiante.setHistoriaAcademica(historia);

    assertThat(estudiante.getHistoriaAcademica()).isEqualTo(historia);
    assertThat(historia.getEstudiante()).isEqualTo(estudiante);
  }

  @Test
  void puedeCrearEstudianteConTodosLosCampos() {
    Estudiante estudiante = Estudiante.builder().dni("123").legajo("LEG001").nombre("Pedro")
                                      .mail("pedro@test.com").build();

    assertThat(estudiante.getDni()).isEqualTo("123");
    assertThat(estudiante.getLegajo()).isEqualTo("LEG001");
    assertThat(estudiante.getNombre()).isEqualTo("Pedro");
    assertThat(estudiante.getMail()).isEqualTo("pedro@test.com");
  }
}