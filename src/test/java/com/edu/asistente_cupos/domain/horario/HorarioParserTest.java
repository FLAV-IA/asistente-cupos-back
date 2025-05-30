package com.edu.asistente_cupos.domain.horario;

import com.edu.asistente_cupos.excepcion.HorarioParseException;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HorarioParserTest {
  @Test
  void parseaUnSoloBloqueCorrectamente() {
    Horario horario = HorarioParser.parse("LUNES 09:00 a 10:00");

    List<RangoHorario> bloques = horario.getBloques();
    assertEquals(1, bloques.size());

    RangoHorario bloque = bloques.get(0);
    assertEquals(DiaSemana.LUNES, bloque.dia());
    assertEquals(LocalTime.of(9, 0), bloque.inicio());
    assertEquals(LocalTime.of(10, 0), bloque.fin());
  }

  @Test
  void parseaMultiplesBloquesSeparadosPorComa() {
    Horario horario = HorarioParser.parse("LUNES 09:00 a 10:00, MARTES 10:00 a 11:00");

    assertEquals(2, horario.getBloques().size());

    assertEquals(DiaSemana.LUNES, horario.getBloques().get(0).dia());
    assertEquals(DiaSemana.MARTES, horario.getBloques().get(1).dia());
  }

  @Test
  void devuelveHorarioVacioSiDescripcionEsNull() {
    Horario horario = HorarioParser.parse(null);
    assertTrue(horario.estaVacio());
  }

  @Test
  void devuelveHorarioVacioSiDescripcionEsVacia() {
    Horario horario = HorarioParser.parse("   ");
    assertTrue(horario.estaVacio());
  }

  @Test
  void lanzaExcepcionSiFaltanPartesDelBloque() {
    HorarioParseException ex = assertThrows(HorarioParseException.class,
      () -> HorarioParser.parse("LUNES 08:00"));
    assertTrue(ex.getMessage().contains("Formato inválido"));
  }

  @Test
  void lanzaExcepcionSiDiaEsInvalido() {
    HorarioParseException ex = assertThrows(HorarioParseException.class,
      () -> HorarioParser.parse("LUNDDDDES 09:00 a 10:00"));
    assertTrue(ex.getMessage().contains("Error al parsear bloque horario"));
  }

  @Test
  void lanzaExcepcionSiHoraTieneFormatoIncorrecto() {
    HorarioParseException ex = assertThrows(HorarioParseException.class,
      () -> HorarioParser.parse("LUNES 09am a 10:00"));
    assertTrue(ex.getMessage().contains("Error al parsear bloque horario"));
  }

  @Test
  void lanzaExcepcionSiFaltanSeparadores() {
    HorarioParseException ex = assertThrows(HorarioParseException.class,
      () -> HorarioParser.parse("LUNES0900a1000"));
    assertTrue(ex.getMessage().contains("Formato inválido"));
  }
}
