package com.edu.asistente_cupos.excepcion.opta;

// Unchecked: para cuando no se quiere propagar throws
public class AsignacionRuntimeException extends RuntimeException {
  public AsignacionRuntimeException(AsignacionException cause) {
    super(cause.getMessage(), cause);
  }

  public AsignacionException getDominioException() {
    return (AsignacionException) getCause();
  }
}
