package com.edu.asistenteCupos.domain.prompt.optimizado;

import lombok.Builder;
import lombok.Data;

/**
 * Representación de la cursada optimizada para un prompt
 */
@Data
@Builder
public class Cursada4Prompt {
  /**
   * código de materia
   */
  private String cm;
  /**
   * fueAprobada
   */
  private Boolean fpm;
}
