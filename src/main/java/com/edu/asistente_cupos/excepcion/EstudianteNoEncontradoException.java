package com.edu.asistente_cupos.excepcion;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EstudianteNoEncontradoException extends RuntimeException {
  public EstudianteNoEncontradoException(String mensaje) {super(mensaje);}
}
