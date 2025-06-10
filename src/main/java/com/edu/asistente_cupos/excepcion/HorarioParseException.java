package com.edu.asistente_cupos.excepcion;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class HorarioParseException extends RuntimeException {
  public HorarioParseException(String mensaje) {
    super(mensaje);
  }

  public HorarioParseException(String mensaje, Throwable causa) {
    super(mensaje, causa);
  }
}
