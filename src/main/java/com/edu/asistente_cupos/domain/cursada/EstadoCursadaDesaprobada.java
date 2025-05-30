package com.edu.asistente_cupos.domain.cursada;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class EstadoCursadaDesaprobada extends EstadoCursada {
  private int nota;

  @Override
  public boolean fueAprobada() {
    return false;
  }

  @Override
  public boolean estaEnCurso() {
    return false;
  }

  @Override
  public int getNota() {
    return nota;
  }
}