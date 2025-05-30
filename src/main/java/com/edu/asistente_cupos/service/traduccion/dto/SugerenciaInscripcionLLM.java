package com.edu.asistente_cupos.service.traduccion.dto;

import lombok.Data;

/**
 * Representa la sugerencia completa con prioridad y decisión final del LLM.
 */
@Data
public class SugerenciaInscripcionLLM {
  /**
   * DNI del estudiante
   */
  private String a;
  /**
   * Codigo de comisión
   */
  private String c;
  /**
   * Prioridad
   */
  private int p;
  /**
   * motivo en lenguaje natural
   */
  private String m;
  /**
   * CupoAsignado
   */
  private boolean x;
}