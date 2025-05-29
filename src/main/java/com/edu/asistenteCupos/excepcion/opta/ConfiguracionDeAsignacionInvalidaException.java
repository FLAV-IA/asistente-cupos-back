package com.edu.asistenteCupos.excepcion.opta;

public class ConfiguracionDeAsignacionInvalidaException extends RuntimeException {
  public ConfiguracionDeAsignacionInvalidaException(String mensaje) {
    super(mensaje, null);
  }
}
