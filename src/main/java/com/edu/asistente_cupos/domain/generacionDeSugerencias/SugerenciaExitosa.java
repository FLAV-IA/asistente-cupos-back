package com.edu.asistente_cupos.domain.generacionDeSugerencias;

import com.edu.asistente_cupos.domain.Comision;
import com.edu.asistente_cupos.domain.Estudiante;
import com.edu.asistente_cupos.domain.Materia;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaAceptada;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaInscripcion;

public class SugerenciaExitosa implements ResultadoDeSugerencias {
  private final Comision comision;

  public SugerenciaExitosa(Comision comision) {
    this.comision = comision;
  }

  @Override
  public SugerenciaInscripcion crearSugerencia(Estudiante estudiante, Materia materia, String motivo, int prioridad) {
    return new SugerenciaAceptada(estudiante, materia, comision, motivo, prioridad);
  }
}