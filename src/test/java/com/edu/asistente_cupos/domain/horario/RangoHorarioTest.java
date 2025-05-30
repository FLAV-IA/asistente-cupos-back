package com.edu.asistente_cupos.domain.horario;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RangoHorarioTest {
  private RangoHorario bloque(DiaSemana dia, String desde, String hasta) {
    return new RangoHorario(dia, LocalTime.parse(desde), LocalTime.parse(hasta));
  }

  @Test
  void bloquesConSuperposicionParcialDevuelvenTrue() {
    RangoHorario r1 = bloque(DiaSemana.LUNES, "09:00", "10:00");
    RangoHorario r2 = bloque(DiaSemana.LUNES, "09:30", "10:30");

    assertTrue(r1.seSuperponeCon(r2));
    assertTrue(r2.seSuperponeCon(r1));
  }

  @Test
  void bloquesEnDiasDistintosNoSeSuperponen() {
    RangoHorario r1 = bloque(DiaSemana.LUNES, "09:00", "10:00");
    RangoHorario r2 = bloque(DiaSemana.MARTES, "09:00", "10:00");

    assertFalse(r1.seSuperponeCon(r2));
  }

  @Test
  void bloquesQueSeTocanEnElBordeSeConsideranSuperpuestos() {
    RangoHorario r1 = bloque(DiaSemana.MIERCOLES, "10:00", "11:00");
    RangoHorario r2 = bloque(DiaSemana.MIERCOLES, "11:00", "12:00");

    assertTrue(r1.seSuperponeCon(r2));
  }

  @Test
  void bloquesDisjuntosNoSeSuperponen() {
    RangoHorario r1 = bloque(DiaSemana.JUEVES, "08:00", "09:00");
    RangoHorario r2 = bloque(DiaSemana.JUEVES, "09:01", "10:00");

    assertFalse(r1.seSuperponeCon(r2));
  }

  @Test
  void toStringDevuelveFormatoEsperado() {
    RangoHorario r = bloque(DiaSemana.VIERNES, "10:00", "12:00");
    assertEquals("VIERNES 10:00 a 12:00", r.toString());
  }

  @Test
  void recordsConMismosValoresSonIguales() {
    RangoHorario r1 = bloque(DiaSemana.SABADO, "09:00", "10:00");
    RangoHorario r2 = bloque(DiaSemana.SABADO, "09:00", "10:00");

    assertEquals(r1, r2);
    assertEquals(r1.hashCode(), r2.hashCode());
  }

  @Test
  void recordsConValoresDistintosNoSonIguales() {
    RangoHorario r1 = bloque(DiaSemana.DOMINGO, "09:00", "10:00");
    RangoHorario r2 = bloque(DiaSemana.DOMINGO, "09:00", "11:00");

    assertNotEquals(r1, r2);
  }
}
