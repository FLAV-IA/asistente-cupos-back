package com.edu.asistenteCupos.service.factory.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PeticionInscripcion4Prompt {
  private String dni;
  private HistoriaAcademica4Prompt historiaAcademica;
  private String codigoMateria;
  private List<String> codigosComisionesPosibles;
  private Boolean cumpleCorrelativa;
}