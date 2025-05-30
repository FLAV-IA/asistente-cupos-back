package com.edu.asistente_cupos.domain;

import com.edu.asistente_cupos.domain.cursada.Cursada;
import com.edu.asistente_cupos.domain.cursada.CursadaFactory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HistoriaAcademicaTest {

  @Test
  void haySuperposicionHoraria_devuelveFalseSiNoTieneCursadasEnCurso() {
    Comision nuevaComision = Comision.builder().horario("Lunes 10:00 a 12:00").build();

    HistoriaAcademica historia = HistoriaAcademica.builder().cursadas(List.of()).build();

    assertFalse(historia.haySuperposicionHoraria(nuevaComision));
  }

  @Test
  void haySuperposicionHoraria_devuelveTrueSiTieneCursadasEnCurso() {
    Materia inscripta = Materia.builder().codigo("MAT123").build();
    Cursada enCurso = CursadaFactory.enCurso(inscripta);

    Comision nuevaComision = Comision.builder().horario("Martes 14:00 a 16:00").build();

    HistoriaAcademica historia = HistoriaAcademica.builder().cursadas(List.of(enCurso)).build();

    assertTrue(historia.haySuperposicionHoraria(nuevaComision));
  }

  @Test
  void cumpleCorrelativas_cuandoTieneTodasLasCorrelativasAprobadas() {
    Materia correlativa = Materia.builder().codigo("MAT1").build();
    Materia materiaDestino = Materia.builder().codigo("MAT2").correlativas(List.of(correlativa))
                                    .build();

    Cursada cursadaAprobada = CursadaFactory.aprobada(correlativa, 10);

    HistoriaAcademica historia = HistoriaAcademica.builder().cursadas(List.of(cursadaAprobada))
                                                  .build();

    assertTrue(historia.cumpleCorrelativas(materiaDestino));
  }

  @Test
  void cumpleCorrelativas_fallaSiFaltaUnaCorrelativa() {
    Materia correlativaFaltante = Materia.builder().codigo("MAT3").build();
    Materia materiaDestino = Materia.builder().codigo("MAT4")
                                    .correlativas(List.of(correlativaFaltante)).build();

    HistoriaAcademica historia = HistoriaAcademica.builder().cursadas(List.of()).build();

    assertFalse(historia.cumpleCorrelativas(materiaDestino));
  }

  @Test
  void cumpleCorrelativas_cuandoNoTieneCorrelativas() {
    Materia materiaDestino = Materia.builder().codigo("MAT5").correlativas(List.of()).build();

    HistoriaAcademica historia = HistoriaAcademica.builder().cursadas(List.of()).build();

    assertTrue(historia.cumpleCorrelativas(materiaDestino));
  }

  @Test
  void cumpleCorrelativas_noCumpleSiLaCursadaNoFueAprobada() {
    Materia correlativa = Materia.builder().codigo("MAT6").build();
    Materia materiaDestino = Materia.builder().codigo("MAT7").correlativas(List.of(correlativa))
                                    .build();

    Cursada cursadaNoAprobada = CursadaFactory.desaprobada(correlativa, 2);

    HistoriaAcademica historia = HistoriaAcademica.builder().cursadas(List.of(cursadaNoAprobada))
                                                  .build();

    assertFalse(historia.cumpleCorrelativas(materiaDestino));
  }

  @Test
  void cumpleCorrelativas_noCumpleSiNoHayCursadasYLaMateriaTieneCorrelativas() {
    Materia correlativa = Materia.builder().codigo("MAT8").build();
    Materia materiaDestino = Materia.builder().codigo("MAT9").correlativas(List.of(correlativa))
                                    .build();

    HistoriaAcademica historia = HistoriaAcademica.builder().cursadas(List.of()).build();

    assertFalse(historia.cumpleCorrelativas(materiaDestino));
  }
}
