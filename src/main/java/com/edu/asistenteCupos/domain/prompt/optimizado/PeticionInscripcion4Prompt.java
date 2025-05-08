package com.edu.asistenteCupos.domain.prompt.optimizado;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Representación de una peticion de inscripcion optimizado para un prompt
 */
@Data
@Builder
public class PeticionInscripcion4Prompt {
  /**
   * dni del estudiante
   */
  private String a;
  /**
   * historiaAcademica
   */
  private HistoriaAcademica4Prompt h;
  /**
   * peticionesDeMaterias
   */
  private List<PeticionDeMateria4Prompt> p;
}