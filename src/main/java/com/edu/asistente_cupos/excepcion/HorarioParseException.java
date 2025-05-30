package com.edu.asistente_cupos.excepcion;

public class HorarioParseException extends RuntimeException {
  public HorarioParseException(String mensaje) {
    super(mensaje);
  }

  public HorarioParseException(String mensaje, Throwable causa) {
    super(mensaje, causa);
  }
}
