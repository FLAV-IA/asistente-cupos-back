package com.edu.asistenteCupos.excepcion;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ComisionNoEncontradaException extends RuntimeException {
  public ComisionNoEncontradaException(String mensaje) {super(mensaje);}
}
