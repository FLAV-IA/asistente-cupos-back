package com.edu.asistente_cupos.domain;

import com.edu.asistente_cupos.domain.cursada.Cursada;
import com.edu.asistente_cupos.domain.cursada.CursadaFactory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HistoriaAcademicaTest {
  @Test
  void retornaFalseSiNoTieneCursadasEnCurso() {
    Comision nuevaComision = Comision.builder().horario("Lunes 10:00 a 12:00").build();
    HistoriaAcademica historia = HistoriaAcademica.builder().cursadas(List.of()).build();

    assertFalse(historia.haySuperposicionHoraria(nuevaComision));
  }

  @Test
  void retornaTrueSiTieneCursadasEnCurso() {
    Materia inscripta = Materia.builder().codigo("MAT123").build();
    Cursada enCurso = CursadaFactory.enCurso(inscripta);
    Comision nuevaComision = Comision.builder().horario("Martes 14:00 a 16:00").build();
    HistoriaAcademica historia = HistoriaAcademica.builder().cursadas(List.of(enCurso)).build();

    assertTrue(historia.haySuperposicionHoraria(nuevaComision));
  }

  @Test
  void validaCorrelativasAprobadasCorrectamente() {
    Materia correlativa = Materia.builder().codigo("MAT1").build();
    Materia materiaDestino = Materia.builder().codigo("MAT2").correlativas(List.of(correlativa))
                                    .build();
    Cursada cursadaAprobada = CursadaFactory.aprobada(correlativa, 10);
    HistoriaAcademica historia = HistoriaAcademica.builder().cursadas(List.of(cursadaAprobada))
                                                  .build();

    assertTrue(historia.cumpleCorrelativas(materiaDestino));
  }

  @Test
  void fallaCuandoFaltaUnaCorrelativa() {
    Materia correlativaFaltante = Materia.builder().codigo("MAT3").build();
    Materia materiaDestino = Materia.builder().codigo("MAT4")
                                    .correlativas(List.of(correlativaFaltante)).build();
    HistoriaAcademica historia = HistoriaAcademica.builder().cursadas(List.of()).build();

    assertFalse(historia.cumpleCorrelativas(materiaDestino));
  }

  @Test
  void retornaTrueCuandoLaMateriaNoTieneCorrelativas() {
    Materia materiaDestino = Materia.builder().codigo("MAT5").correlativas(List.of()).build();
    HistoriaAcademica historia = HistoriaAcademica.builder().cursadas(List.of()).build();

    assertTrue(historia.cumpleCorrelativas(materiaDestino));
  }

  @Test
  void retornaFalseSiLaCursadaNoFueAprobada() {
    Materia correlativa = Materia.builder().codigo("MAT6").build();
    Materia materiaDestino = Materia.builder().codigo("MAT7").correlativas(List.of(correlativa))
                                    .build();
    Cursada cursadaNoAprobada = CursadaFactory.desaprobada(correlativa, 2);
    HistoriaAcademica historia = HistoriaAcademica.builder().cursadas(List.of(cursadaNoAprobada))
                                                  .build();

    assertFalse(historia.cumpleCorrelativas(materiaDestino));
  }

  @Test
  void retornaFalseSiNoTieneCursadasYLaMateriaRequiereCorrelativas() {
    Materia correlativa = Materia.builder().codigo("MAT8").build();
    Materia materiaDestino = Materia.builder().codigo("MAT9").correlativas(List.of(correlativa))
                                    .build();
    HistoriaAcademica historia = HistoriaAcademica.builder().cursadas(List.of()).build();

    assertFalse(historia.cumpleCorrelativas(materiaDestino));
  }

  @Test
  void inscripcionesActualesRetornaListaVaciaSiNoTieneCursadas() {
    HistoriaAcademica historia = HistoriaAcademica.builder().cursadas(null).build();

    assertTrue(historia.inscripcionesActuales().isEmpty());
  }

  @Test
  void inscripcionesActualesFiltraSoloMateriasEnCurso() {
    Materia matA = Materia.builder().codigo("A").build();
    Materia matB = Materia.builder().codigo("B").build();

    Cursada enCurso = CursadaFactory.enCurso(matA);
    Cursada aprobada = CursadaFactory.aprobada(matB, 9);

    HistoriaAcademica historia = HistoriaAcademica.builder().cursadas(List.of(enCurso, aprobada))
                                                  .build();

    List<Materia> actuales = historia.inscripcionesActuales().stream().map(Cursada::getMateria)
                                     .toList();
    assertEquals(1, actuales.size());
    assertTrue(actuales.contains(matA));
  }

  @Test
  void cumpleCorrelativasIgnoraDuplicadosEnLaLista() {
    Materia correlativa = Materia.builder().codigo("MAT10").build();
    Materia destino = Materia.builder().codigo("MAT11")
                             .correlativas(List.of(correlativa, correlativa)).build();

    Cursada aprobada = CursadaFactory.aprobada(correlativa, 10);
    HistoriaAcademica historia = HistoriaAcademica.builder().cursadas(List.of(aprobada)).build();

    assertTrue(historia.cumpleCorrelativas(destino));
  }

  @Test
  void construyeHistoriaConTodosLosCamposSeteados() {
    Estudiante estudiante = Estudiante.builder().dni("123").build();

    HistoriaAcademica historia = HistoriaAcademica.builder().coeficiente(8.5)
                                                  .totalHistoricasAprobadas(10)
                                                  .totalInscripcionesHistoricas(12)
                                                  .estudiante(estudiante).build();

    assertEquals(8.5, historia.getCoeficiente());
    assertEquals(10, historia.getTotalHistoricasAprobadas());
    assertEquals(12, historia.getTotalInscripcionesHistoricas());
    assertEquals(estudiante, historia.getEstudiante());
  }

  @Test
  void retornaFalseSiSoloUnaDeLasCorrelativasEstaAprobada() {
    Materia cor1 = Materia.builder().codigo("MAT12").build();
    Materia cor2 = Materia.builder().codigo("MAT13").build();
    Materia destino = Materia.builder().codigo("MAT14").correlativas(List.of(cor1, cor2)).build();

    Cursada aprobadaCor1 = CursadaFactory.aprobada(cor1, 8);
    Cursada noAprobadaCor2 = CursadaFactory.desaprobada(cor2, 3);

    HistoriaAcademica historia = HistoriaAcademica.builder()
                                                  .cursadas(List.of(aprobadaCor1, noAprobadaCor2))
                                                  .build();

    assertFalse(historia.cumpleCorrelativas(destino));
  }

  @Test
  void cumpleCorrelativasRetornaFalseSiCursadasEsNull() {
    Materia correlativa = Materia.builder().codigo("MAT-X").build();
    Materia destino = Materia.builder().codigo("MAT-Y").correlativas(List.of(correlativa)).build();

    HistoriaAcademica historia = HistoriaAcademica.builder().cursadas(null).build();

    assertFalse(historia.cumpleCorrelativas(destino));
  }

  @Test
  void materiasEnCursoRetornaSoloMateriasConEstadoEnCurso() {
    Materia matEnCurso = Materia.builder().codigo("MAT-A").build();
    Materia matAprobada = Materia.builder().codigo("MAT-B").build();

    Cursada cursadaEnCurso = CursadaFactory.enCurso(matEnCurso);
    Cursada cursadaAprobada = CursadaFactory.aprobada(matAprobada, 9);

    HistoriaAcademica historia = HistoriaAcademica.builder().cursadas(
      List.of(cursadaEnCurso, cursadaAprobada)).build();

    List<Materia> materias = historia.materiasEnCurso();

    assertEquals(1, materias.size());
    assertTrue(materias.contains(matEnCurso));
  }
}
