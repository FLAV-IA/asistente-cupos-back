package com.edu.asistente_cupos.domain;

import com.edu.asistente_cupos.domain.cursada.Cursada;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EstudianteTest {
  @Test
  void puedeInscribirseDelegadoAHistoriaAcademica() {
    Materia materia = Materia.builder().codigo("MAT1").build();

    HistoriaAcademica historiaMock = mock(HistoriaAcademica.class);
    when(historiaMock.cumpleCorrelativas(materia)).thenReturn(true);

    Estudiante estudiante = Estudiante.builder().dni("12345678").build();
    estudiante.setHistoriaAcademica(historiaMock);

    assertThat(estudiante.puedeInscribirse(materia)).isTrue();
    verify(historiaMock).cumpleCorrelativas(materia);
  }

  @Test
  void estaInscriptoEnMasDeDelegadoAHistoriaAcademica() {
    HistoriaAcademica historiaMock = mock(HistoriaAcademica.class);
    when(historiaMock.inscripcionesActuales()).thenReturn(
      List.of(mock(Cursada.class), mock(Cursada.class)));

    Estudiante estudiante = Estudiante.builder().dni("111").build();
    estudiante.setHistoriaAcademica(historiaMock);

    assertThat(estudiante.estaInscriptoEnMasDe(1)).isTrue();
    verify(historiaMock).inscripcionesActuales();
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