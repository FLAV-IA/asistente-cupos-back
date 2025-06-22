package com.edu.asistente_cupos.domain.sugerencia;

import com.edu.asistente_cupos.domain.Comision;
import com.edu.asistente_cupos.domain.Estudiante;
import com.edu.asistente_cupos.domain.Materia;

 sealed public interface SugerenciaInscripcion permits SugerenciaAceptada, SugerenciaRechazada {
  Estudiante estudiante();

  Materia materia();
  Comision comision();

  String motivo();

  int prioridad();

  boolean fueAceptada();
}

