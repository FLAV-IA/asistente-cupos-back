package com.edu.asistente_cupos.excepcion.opta;

public class InterrupcionDuranteGeneracionSugerenciaException extends GeneracionSugerenciaException {
  public InterrupcionDuranteGeneracionSugerenciaException(Throwable cause) {
    super("El proceso de asignación fue interrumpido", cause);
  }
}

