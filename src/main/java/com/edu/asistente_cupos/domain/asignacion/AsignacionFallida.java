package com.edu.asistente_cupos.domain.asignacion;

import com.edu.asistente_cupos.domain.Comision;
import com.edu.asistente_cupos.domain.Estudiante;
import com.edu.asistente_cupos.domain.Materia;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaInscripcion;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaRechazada;

public class AsignacionFallida implements ResultadoAsignacion {
  private final Comision comision;

  public AsignacionFallida(Comision comision) {
    this.comision = comision;
  }


  @Override
  public SugerenciaInscripcion crearSugerencia(Estudiante estudiante, Materia materia, String motivo, int prioridad) {
    return new SugerenciaRechazada(estudiante, materia,comision, motivo, prioridad);
  }
}