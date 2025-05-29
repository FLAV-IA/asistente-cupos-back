package com.edu.asistente_cupos.domain.asignacion;

import com.edu.asistente_cupos.domain.Comision;
import com.edu.asistente_cupos.domain.Estudiante;
import com.edu.asistente_cupos.domain.Materia;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaAsignada;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaInscripcion;

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