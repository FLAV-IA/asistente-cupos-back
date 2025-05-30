package com.edu.asistente_cupos.domain.horario;

public enum DiaSemana {
  LUNES, MARTES, MIERCOLES, JUEVES, VIERNES, SABADO, DOMINGO;

  public static DiaSemana desdeTexto(String texto) {
    return DiaSemana.valueOf(texto.trim().toUpperCase());
  }
}
