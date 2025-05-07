package com.edu.asistenteCupos.domain.prompt;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Representación de una petición de materia optimizada para un prompt
 */
@Data
@Builder
public class PeticionDeMateria4Prompt {
  /**
   * lista de codigosDeComisiones
   */
  private List<String> m;
  /**
   * cumpleCorrelativas
   */
  private boolean c;
}
