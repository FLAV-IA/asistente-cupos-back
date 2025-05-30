package com.edu.asistente_cupos.service.asignacion.manual;

import com.edu.asistente_cupos.domain.Comision;
import com.edu.asistente_cupos.domain.Materia;
import com.edu.asistente_cupos.domain.horario.HorarioParser;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GestorDeCuposTest {
  @Test
  void asignaCupoCorrectamenteSiHayDisponibles() {
    Comision comision = crearComision("COM1", 2);
    GestorDeCupos gestor = new GestorDeCupos(List.of(comision));

    boolean asignado = gestor.asignarCupo("COM1");

    assertThat(asignado).isTrue();
  }

  @Test
  void noAsignaCupoSiNoHayDisponibles() {
    Comision comision = crearComision("COM1", 0);
    GestorDeCupos gestor = new GestorDeCupos(List.of(comision));

    boolean asignado = gestor.asignarCupo("COM1");

    assertThat(asignado).isFalse();
  }

  @Test
  void noAsignaCupoSiLaComisionNoExiste() {
    Comision comision = crearComision("COM1", 3);
    GestorDeCupos gestor = new GestorDeCupos(List.of(comision));

    boolean asignado = gestor.asignarCupo("COM2");

    assertThat(asignado).isFalse();
  }

  @Test
  void reduceCupoEnCadaAsignacionExitosa() {
    Comision comision = crearComision("COM1", 2);
    GestorDeCupos gestor = new GestorDeCupos(List.of(comision));

    boolean asignado1 = gestor.asignarCupo("COM1");
    boolean asignado2 = gestor.asignarCupo("COM1");
    boolean asignado3 = gestor.asignarCupo("COM1");

    assertThat(asignado1).isTrue();
    assertThat(asignado2).isTrue();
    assertThat(asignado3).isFalse();
  }

  private Comision crearComision(String codigo, int cupo) {
    Materia materia = Materia.builder().codigo("MAT1").nombre("Ejemplo").build();
    return new Comision(codigo, HorarioParser.parse("LUNES 09:00 a 10:00"), cupo, materia);
  }
}
