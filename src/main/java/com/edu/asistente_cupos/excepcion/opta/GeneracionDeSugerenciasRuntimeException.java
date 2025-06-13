package com.edu.asistente_cupos.excepcion.opta;

// Unchecked: para cuando no se quiere propagar throws
public class GeneracionDeSugerenciasRuntimeException extends RuntimeException {
  public GeneracionDeSugerenciasRuntimeException(GeneracionSugerenciaException cause) {
    super(cause.getMessage(), cause);
  }

  public GeneracionSugerenciaException getDominioException() {
    return (GeneracionSugerenciaException) getCause();
  }
}
