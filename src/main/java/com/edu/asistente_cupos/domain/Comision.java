package com.edu.asistente_cupos.domain;

import com.edu.asistente_cupos.domain.horario.Horario;
import com.edu.asistente_cupos.mapper.HorarioConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comision {
  @Id
  private String codigo;

  @Convert(converter = HorarioConverter.class)
  private Horario horario;

  private int cupo;

  @ManyToOne
  @JoinColumn(name = "codigo-materia", referencedColumnName = "codigo")
  private Materia materia;

  @Override
  public int hashCode() {
    return Objects.hash(codigo);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    Comision that = (Comision) o;
    return Objects.equals(codigo, that.codigo);
  }

  public boolean tieneCupo() {
    return this.cupo > 0;
  }
}
