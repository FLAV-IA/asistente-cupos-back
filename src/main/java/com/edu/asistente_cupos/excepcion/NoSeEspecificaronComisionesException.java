package com.edu.asistente_cupos.excepcion;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NoSeEspecificaronComisionesException extends RuntimeException {
  public NoSeEspecificaronComisionesException(String mensaje) {
    super(mensaje);
  }
}