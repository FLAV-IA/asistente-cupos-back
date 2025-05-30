package com.edu.asistente_cupos.excepcion.opta;

public class InterrupcionDuranteAsignacionException extends AsignacionException {
  public InterrupcionDuranteAsignacionException(Throwable cause) {
    super("El proceso de asignación fue interrumpido", cause);
  }
}

