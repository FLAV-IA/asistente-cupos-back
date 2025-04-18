package com.edu.asistenteCupos.controller.dto;

import lombok.Data;

@Data
public class SugerenciaInscripcionDto {
  private String nombreEstudiante;

  private String codigoComision;

  private boolean cupoAsignado;

  private String motivo;

  private int prioridad;
}
