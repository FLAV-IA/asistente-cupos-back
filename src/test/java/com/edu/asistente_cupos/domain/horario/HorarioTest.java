package com.edu.asistente_cupos.domain.horario;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HorarioTest {
  private RangoHorario bloque(DiaSemana dia, String desde, String hasta) {
    return new RangoHorario(dia, LocalTime.parse(desde), LocalTime.parse(hasta));
  }

  @Test
  void horarioVacio_noTieneBloques() {
    Horario horario = new Horario(List.of());
    assertTrue(horario.estaVacio());
    assertEquals(0, horario.bloques().size());
  }

  @Test
  void horarioNoVacio_tieneBloques() {
    Horario horario = new Horario(List.of(bloque(DiaSemana.LUNES, "08:00", "09:00")));
    assertFalse(horario.estaVacio());
    assertEquals(1, horario.bloques().size());
  }

  @Test
  void horariosQueCompartenUnBloqueSeSuperponen() {
    Horario h1 = new Horario(List.of(bloque(DiaSemana.MARTES, "10:00", "11:00")));
    Horario h2 = new Horario(List.of(bloque(DiaSemana.MARTES, "10:30", "11:30")));
    assertTrue(h1.superponeCon(h2));
    assertTrue(h2.superponeCon(h1)); // simetría
  }

  @Test
  void horariosEnDiasDistintos_noSeSuperponen() {
    Horario h1 = new Horario(List.of(bloque(DiaSemana.LUNES, "08:00", "09:00")));
    Horario h2 = new Horario(List.of(bloque(DiaSemana.MARTES, "08:00", "09:00")));
    assertFalse(h1.superponeCon(h2));
  }

  @Test
  void horariosContiguosEnMismoDia_seConsideranSuperpuestos() {
    Horario h1 = new Horario(List.of(bloque(DiaSemana.JUEVES, "10:00", "11:00")));
    Horario h2 = new Horario(List.of(bloque(DiaSemana.JUEVES, "11:00", "12:00")));
    assertTrue(h1.superponeCon(h2));
  }

  @Test
  void horariosDisjuntos_noSeSuperponen() {
    Horario h1 = new Horario(List.of(bloque(DiaSemana.VIERNES, "08:00", "09:00")));
    Horario h2 = new Horario(List.of(bloque(DiaSemana.VIERNES, "09:01", "10:00")));
    assertFalse(h1.superponeCon(h2));
  }

  @Test
  void igualdadEntreHorariosDependeDeBloques() {
    Horario h1 = new Horario(List.of(bloque(DiaSemana.MIERCOLES, "10:00", "12:00")));
    Horario h2 = new Horario(List.of(bloque(DiaSemana.MIERCOLES, "10:00", "12:00")));
    assertEquals(h1, h2);
    assertEquals(h1.hashCode(), h2.hashCode());
  }

  @Test
  void horariosDistintosNoSonIguales() {
    Horario h1 = new Horario(List.of(bloque(DiaSemana.MIERCOLES, "10:00", "12:00")));
    Horario h2 = new Horario(List.of(bloque(DiaSemana.MIERCOLES, "11:00", "12:00")));
    assertNotEquals(h1, h2);
  }

  @Test
  void toStringMuestraBloquesSeparadosPorComas() {
    Horario horario = new Horario(List.of(bloque(DiaSemana.LUNES, "08:00", "09:00"),
      bloque(DiaSemana.MARTES, "10:00", "11:00")));

    String toString = horario.toString();
    assertTrue(toString.contains("LUNES 08:00 a 09:00"));
    assertTrue(toString.contains("MARTES 10:00 a 11:00"));
    assertTrue(toString.contains(","));
  }
}