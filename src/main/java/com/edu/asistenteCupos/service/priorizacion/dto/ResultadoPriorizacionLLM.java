package com.edu.asistenteCupos.service.priorizacion.dto;

import lombok.Data;

import java.util.List;

/**
 * Representación de la priorización hecha por el LLM para el estudiante
 */
@Data
public class ResultadoPriorizacionLLM {
  /**
   * Dni del estudiante
   */
  private String a;
  /**
   * Prioridad
   */
  private int p;
  /**
   * La lista de etiquetas que explican la prioridad asignada
   */
  private List<String> e;
}