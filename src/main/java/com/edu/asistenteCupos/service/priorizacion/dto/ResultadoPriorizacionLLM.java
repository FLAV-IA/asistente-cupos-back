package com.edu.asistenteCupos.service.priorizacion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Representación de la priorización hecha por el LLM para el estudiante
 */
@Data
@AllArgsConstructor
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