package com.edu.asistenteCupos.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comision {
  @Id
  private String codigo;
  private String horario;
  private int cupo;

  @ManyToOne
  @JoinColumn(name = "codigo-materia", referencedColumnName = "codigo")
  private Materia materia;
}
