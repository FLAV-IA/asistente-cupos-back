package com.edu.asistente_cupos.domain.horario;

import java.time.LocalTime;
import java.util.Objects;

public record RangoHorario(DiaSemana dia, LocalTime inicio, LocalTime fin) {
  public boolean seSuperponeCon(RangoHorario otro) {
    return this.dia == otro.dia && !this.inicio.isAfter(otro.fin) &&
      !this.fin.isBefore(otro.inicio);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof RangoHorario that))
      return false;
    return dia == that.dia && Objects.equals(inicio, that.inicio) && Objects.equals(fin, that.fin);
  }

  @Override
  public String toString() {
    return dia + " " + inicio + " a " + fin;
  }
}