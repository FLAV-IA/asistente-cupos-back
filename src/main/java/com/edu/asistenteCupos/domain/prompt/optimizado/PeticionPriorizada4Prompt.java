package com.edu.asistenteCupos.domain.prompt.optimizado;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PeticionPriorizada4Prompt {
  /**
   * Dni del estudiante
   */
  private String a;
  /**
   * Codigo de comision
   */
  private String c;
  /**
   * prioridad asignada
   */
  private int p;
  /**
   * motivos en tags
   */
  private String m;
  /**
   * cupo asignado?
   */
  private boolean x;
}