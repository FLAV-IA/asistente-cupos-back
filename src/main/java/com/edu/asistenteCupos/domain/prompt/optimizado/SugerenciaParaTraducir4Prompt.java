package com.edu.asistenteCupos.domain.prompt.optimizado;

import lombok.Builder;
import lombok.Data;

/**
 * Representacion optimizada de una sugerencia para ser traducida y enviada a un prompt
 */
@Data
@Builder
public class SugerenciaParaTraducir4Prompt {
  /**
   * Dni de estudiante
   */
  private String a;
  /**
   * Codigo de comisión
   */
  private String m;
  /**
   * cupoAsignado
   */
  private boolean x;
  /**
   * prioridad
   */
  private int p;
  /**
   * Motivo
   */
  private String e;
}
