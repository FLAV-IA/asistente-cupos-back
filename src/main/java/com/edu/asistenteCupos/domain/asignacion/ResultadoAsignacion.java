package com.edu.asistenteCupos.domain.asignacion;

import com.edu.asistenteCupos.domain.Estudiante;
import com.edu.asistenteCupos.domain.Materia;
import com.edu.asistenteCupos.domain.sugerencia.SugerenciaInscripcion;

public interface ResultadoAsignacion {
  SugerenciaInscripcion crearSugerencia(Estudiante estudiante, Materia materia, String motivo, int prioridad);
}