package com.edu.asistente_cupos.domain.horario;

import com.edu.asistente_cupos.excepcion.HorarioParseException;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HorarioParser {
  public static Horario parse(String descripcion) {
    if (descripcion == null || descripcion.isBlank()) {
      return new Horario(List.of());
    }

    List<RangoHorario> bloques = Arrays.stream(descripcion.split(",")).map(String::trim)
                                       .map(HorarioParser::parsearBloque)
                                       .collect(Collectors.toList());

    return new Horario(bloques);
  }

  private static RangoHorario parsearBloque(String bloqueHorario) {
    String[] partes = bloqueHorario.trim().split(" ");
    if (partes.length < 4) {
      throw new HorarioParseException(
        "Formato inválido: se esperaban al menos 4 partes en '" + bloqueHorario + "'");
    }

    try {
      DiaSemana dia = DiaSemana.desdeTexto(partes[0]);
      LocalTime horaInicio = LocalTime.parse(partes[1]);
      LocalTime horaFin = LocalTime.parse(partes[3]);
      return new RangoHorario(dia, horaInicio, horaFin);
    } catch (Exception e) {
      throw new HorarioParseException("Error al parsear bloque horario: '" + bloqueHorario + "'",
        e);
    }
  }
}