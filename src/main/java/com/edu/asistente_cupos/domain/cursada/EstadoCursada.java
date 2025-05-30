package com.edu.asistente_cupos.domain.cursada;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class EstadoCursada {
  @Id
  @GeneratedValue
  private Long id;

  public abstract boolean fueAprobada();

  public abstract boolean estaEnCurso();

  public abstract int getNota();
}
