package com.edu.asistente_cupos.domain.cursada;

import jakarta.persistence.Entity;

@Entity
public class EstadoCursadaEnCurso extends EstadoCursada {
  @Override
  public boolean fueAprobada() {
    return false;
  }

  @Override
  public boolean estaEnCurso() {
    return true;
  }

  @Override
  public int getNota() {
    throw new IllegalStateException("La cursada en curso no tiene nota.");
  }
}
