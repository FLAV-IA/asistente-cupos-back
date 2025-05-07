package com.edu.asistenteCupos.domain.prompt;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Representación de la historia académica optimizada para un prompt
 */
@Data
@Builder
public class HistoriaAcademica4Prompt {
  /**
   * totalInscripcionesHistoricas
   */
  private String i;
  /**
   totalHistoricasAprobadas
   */
  private String ap;
  /**
   * coeficiente
   */
  private String cf;
  /**
   * cursadasAnteriores
   */
  private List<String> ca;
  /**
   * codigosMateriasInscriptasActuales
   */
  private List<String> ac;
}
