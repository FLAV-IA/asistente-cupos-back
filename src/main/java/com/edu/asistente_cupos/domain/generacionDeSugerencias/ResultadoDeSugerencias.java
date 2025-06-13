package com.edu.asistente_cupos.domain.generacionDeSugerencias;

import com.edu.asistente_cupos.domain.Estudiante;
import com.edu.asistente_cupos.domain.Materia;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaInscripcion;

public interface ResultadoDeSugerencias {
  SugerenciaInscripcion crearSugerencia(Estudiante estudiante, Materia materia, String motivo, int prioridad);
}