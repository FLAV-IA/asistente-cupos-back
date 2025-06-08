package com.edu.asistente_cupos.domain.sugerencia;

import com.edu.asistente_cupos.domain.Estudiante;
import com.edu.asistente_cupos.domain.Materia;

public sealed interface SugerenciaInscripcion permits SugerenciaAceptada, SugerenciaRechazada {
  Estudiante estudiante();

  Materia materia();

  String motivo();

  int prioridad();

  boolean fueAceptada();
}

