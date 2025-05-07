package com.edu.asistenteCupos.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SugerenciaInscripcionDTO {
  private String nombreEstudiante;
  private String dniEstudiante;
  private String nombreMateria;
  private String codigoComision;
  private String motivo;
  private int prioridad;
  private boolean cupoAsignado;
}