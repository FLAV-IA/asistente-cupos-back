package com.edu.asistente_cupos.excepcion.opta;

public class ErrorDeEjecucionDeAsignacionException extends AsignacionException {
  public ErrorDeEjecucionDeAsignacionException(Throwable cause) {
    super("Ocurrió un error al ejecutar el solver de asignación", cause);
  }
}