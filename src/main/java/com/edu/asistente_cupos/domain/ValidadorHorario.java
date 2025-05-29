package com.edu.asistente_cupos.domain;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class ValidadorHorario {
  private ValidadorHorario() {}

  public static boolean haySuperposicion(String horario1, String horario2) {
    List<RangoHorario> rangos1 = parsearHorarios(horario1);
    List<RangoHorario> rangos2 = parsearHorarios(horario2);

    return rangos1.stream().anyMatch(r1 -> rangos2.stream().anyMatch(r1::seSuperponeCon));
  }

  private static List<RangoHorario> parsearHorarios(String horarioStr) {
    if (horarioStr == null || horarioStr.isBlank())
      return List.of();

    return List.of(horarioStr.split(",")).stream().map(String::trim)
               .map(ValidadorHorario::parsearBloque).collect(Collectors.toList());
  }

  private static RangoHorario parsearBloque(String bloque) {
    String[] partes = bloque.trim().split(" ");
    String dia = partes[0];
    LocalTime inicio = LocalTime.parse(partes[1]);
    LocalTime fin = LocalTime.parse(partes[3]);
    return new RangoHorario(dia, inicio, fin);
  }

  static class RangoHorario {
    final String dia;
    final LocalTime inicio;
    final LocalTime fin;

    RangoHorario(String dia, LocalTime inicio, LocalTime fin) {
      this.dia = dia;
      this.inicio = inicio;
      this.fin = fin;
    }

    boolean seSuperponeCon(RangoHorario otro) {
      return this.dia.equalsIgnoreCase(otro.dia) && !inicio.isAfter(otro.fin) &&
        !fin.isBefore(otro.inicio);
    }
  }
}
