package com.edu.asistente_cupos.excepcion;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ComisionesDeDistintaMateriaException extends RuntimeException {
  public ComisionesDeDistintaMateriaException(String mensaje) {
    super(mensaje);
  }
}