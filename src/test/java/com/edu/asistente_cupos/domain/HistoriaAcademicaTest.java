package com.edu.asistente_cupos.domain;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HistoriaAcademicaTest {
  @Test
  void haySuperposicionHoraria_devuelveFalseSiNoTieneInscripciones() {
    Comision nuevaComision = Comision.builder().horario("Lunes 10:00 a 12:00").build();

    HistoriaAcademica historia = HistoriaAcademica.builder()
                                                  .inscripcionesActuales(Set.of()) // vacío
                                                  .build();

    assertFalse(historia.haySuperposicionHoraria(nuevaComision));
  }

  @Test
  void haySuperposicionHoraria_devuelveTrueSiTieneInscripcionesAunqueNoCompareHorarios() {
    Materia inscripta = Materia.builder().codigo("MAT123").build();

    Comision nuevaComision = Comision.builder().horario("Martes 14:00 a 16:00").build();

    HistoriaAcademica historia = HistoriaAcademica.builder().inscripcionesActuales(
                                                    Set.of(inscripta)) // con una materia inscripta
                                                  .build();

    assertTrue(historia.haySuperposicionHoraria(nuevaComision));
  }

  @Test
  void cumpleCorrelativas_cuandoTieneTodasLasCorrelativasAprobadas() {
    Materia correlativa = Materia.builder().codigo("MAT1").build();
    Materia materiaDestino = Materia.builder().codigo("MAT2").correlativas(List.of(correlativa))
                                    .build();

    Cursada cursadaAprobada = Cursada.builder().materia(correlativa).fueAprobada(true).build();

    HistoriaAcademica historia = HistoriaAcademica.builder()
                                                  .cursadasAnteriores(List.of(cursadaAprobada))
                                                  .build();

    assertTrue(historia.cumpleCorrelativas(materiaDestino));
  }

  @Test
  void cumpleCorrelativas_fallaSiFaltaUnaCorrelativa() {
    Materia correlativaFaltante = Materia.builder().codigo("MAT3").build();
    Materia materiaDestino = Materia.builder().codigo("MAT4")
                                    .correlativas(List.of(correlativaFaltante)).build();

    HistoriaAcademica historia = HistoriaAcademica.builder().cursadasAnteriores(
                                                    List.of()) // no tiene cursadas
                                                  .build();

    assertFalse(historia.cumpleCorrelativas(materiaDestino));
  }

  @Test
  void cumpleCorrelativas_cuandoNoTieneCorrelativas() {
    Materia materiaDestino = Materia.builder().codigo("MAT5")
                                    .correlativas(List.of()) // sin correlativas
                                    .build();

    HistoriaAcademica historia = HistoriaAcademica.builder()
                                                  .cursadasAnteriores(List.of()) // irrelevante
                                                  .build();

    assertTrue(historia.cumpleCorrelativas(materiaDestino));
  }

  @Test
  void cumpleCorrelativas_noCumpleSiLaCursadaNoFueAprobada() {
    Materia correlativa = Materia.builder().codigo("MAT6").build();
    Materia materiaDestino = Materia.builder().codigo("MAT7").correlativas(List.of(correlativa))
                                    .build();

    Cursada cursadaNoAprobada = Cursada.builder().materia(correlativa).fueAprobada(false).build();

    HistoriaAcademica historia = HistoriaAcademica.builder()
                                                  .cursadasAnteriores(List.of(cursadaNoAprobada))
                                                  .build();

    assertFalse(historia.cumpleCorrelativas(materiaDestino));
  }

  @Test
  void cumpleCorrelativas_noCumpleSiNoHayCursadasYLaMateriaTieneCorrelativas() {
    Materia correlativa = Materia.builder().codigo("MAT8").build();
    Materia materiaDestino = Materia.builder().codigo("MAT9").correlativas(List.of(correlativa))
                                    .build();

    HistoriaAcademica historia = HistoriaAcademica.builder().cursadasAnteriores(List.of()) // vacía
                                                  .build();

    assertFalse(historia.cumpleCorrelativas(materiaDestino));
  }
}
