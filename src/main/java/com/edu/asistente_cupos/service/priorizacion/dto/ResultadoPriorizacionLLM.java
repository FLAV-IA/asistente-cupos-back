package com.edu.asistente_cupos.service.priorizacion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Representación de la priorización hecha por el LLM para el estudiante
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultadoPriorizacionLLM {
  /**
   * Dni del estudiante
   */
  private String a;
  /**
   * Lista de priorizaciones por materia solicitada
   */
  private List<EvaluacionPrioridad> ep;

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class EvaluacionPrioridad {
    /**
     * Código de la materia
     */
    private String n;

    /**
     * Prioridad asignada
     */
    private int p;

    /**
     * Lista de etiquetas que justifican la prioridad
     */
    private List<String> e;
  }
}