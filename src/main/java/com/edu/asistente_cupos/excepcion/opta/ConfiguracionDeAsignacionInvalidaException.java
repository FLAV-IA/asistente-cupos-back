package com.edu.asistente_cupos.excepcion.opta;

public class ConfiguracionDeAsignacionInvalidaException extends RuntimeException {
  public ConfiguracionDeAsignacionInvalidaException(String mensaje) {
    super(mensaje, null);
  }
}
