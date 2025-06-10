package com.edu.asistente_cupos.excepcion;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Se lanza cuando la API de OpenAI devuelve un error de cuota insuficiente.
 */
@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
public class CuotaDeOpenAiInsuficienteException extends RuntimeException {
  public CuotaDeOpenAiInsuficienteException(String mensaje, Throwable causa) {
    super(mensaje, causa);
  }
}
