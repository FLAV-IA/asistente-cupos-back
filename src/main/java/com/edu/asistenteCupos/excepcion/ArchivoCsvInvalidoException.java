package com.edu.asistenteCupos.excepcion;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ArchivoCsvInvalidoException extends RuntimeException {
  public ArchivoCsvInvalidoException(String mensaje, Throwable causa) {
    super(mensaje, causa);
  }
}
