package com.edu.asistenteCupos.domain.asignacion;

import com.edu.asistenteCupos.domain.Estudiante;
import com.edu.asistenteCupos.domain.Materia;
import com.edu.asistenteCupos.domain.sugerencia.SugerenciaInscripcion;
import com.edu.asistenteCupos.domain.sugerencia.SugerenciaRechazada;

public class AsignacionFallida implements ResultadoAsignacion {

  @Override
  public SugerenciaInscripcion crearSugerencia(Estudiante estudiante, Materia materia, String motivo, int prioridad) {
    return new SugerenciaRechazada(estudiante, materia, motivo, prioridad);
  }
}