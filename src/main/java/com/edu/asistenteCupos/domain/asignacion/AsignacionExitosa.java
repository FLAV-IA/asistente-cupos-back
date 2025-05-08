package com.edu.asistenteCupos.domain.asignacion;

import com.edu.asistenteCupos.domain.Comision;
import com.edu.asistenteCupos.domain.Estudiante;
import com.edu.asistenteCupos.domain.Materia;
import com.edu.asistenteCupos.domain.sugerencia.SugerenciaAsignada;
import com.edu.asistenteCupos.domain.sugerencia.SugerenciaInscripcion;

public class AsignacionExitosa implements ResultadoAsignacion {
  private final Comision comision;

  public AsignacionExitosa(Comision comision) {
    this.comision = comision;
  }

  @Override
  public SugerenciaInscripcion crearSugerencia(Estudiante estudiante, Materia materia, String motivo, int prioridad) {
    return new SugerenciaAsignada(estudiante, materia, comision, motivo, prioridad);
  }
}