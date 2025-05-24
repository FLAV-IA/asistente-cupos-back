package com.edu.asistenteCupos.domain.sugerencia;

import com.edu.asistenteCupos.domain.Comision;
import com.edu.asistenteCupos.domain.Estudiante;
import com.edu.asistenteCupos.domain.Materia;

public record SugerenciaRechazada(Estudiante estudiante, Materia materia, Comision comision, String motivo,
                                  int prioridad) implements SugerenciaInscripcion {
  @Override
  public boolean fueAsignada() {
    return false;
  }
}