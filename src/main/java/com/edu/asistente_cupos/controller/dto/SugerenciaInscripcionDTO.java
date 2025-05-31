package com.edu.asistente_cupos.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class SugerenciaInscripcionDTO {
  private String nombreEstudiante;
  private String dniEstudiante;
  private String nombreMateria;
  private String codigoComision;
  private String motivo;
  private int prioridad;
  private boolean cupoAsignado;
  private HistoriaAcademicaDTO historiaAcademica;
}