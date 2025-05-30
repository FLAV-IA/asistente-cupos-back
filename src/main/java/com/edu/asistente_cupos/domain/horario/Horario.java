package com.edu.asistente_cupos.domain.horario;

import java.util.List;
import java.util.Objects;

public record Horario(List<RangoHorario> bloques) {
  public Horario(List<RangoHorario> bloques) {
    this.bloques = List.copyOf(bloques);
  }

  public boolean superponeCon(Horario otro) {
    return bloques.stream().anyMatch(b1 -> otro.bloques.stream().anyMatch(b1::seSuperponeCon));
  }

  public boolean estaVacio() {
    return bloques.isEmpty();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof Horario horario))
      return false;
    return Objects.equals(bloques, horario.bloques);
  }

  @Override
  public String toString() {
    return String.join(", ", bloques.stream().map(RangoHorario::toString).toList());
  }
}