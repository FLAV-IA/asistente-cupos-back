package com.edu.asistente_cupos.excepcion.opta;

public class ErrorDeEjecucionDeGeneracionSugerenciaException extends GeneracionSugerenciaException {
  public ErrorDeEjecucionDeGeneracionSugerenciaException(Throwable cause) {
    super("Ocurrió un error al ejecutar el solver de asignación", cause);
  }
}