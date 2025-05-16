package com.edu.asistenteCupos.service.factory.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class HistoriaAcademica4Prompt {
  private String totalInscripcionesHistoricas;
  private String totalHistoricasAprobadas;
  private String coeficiente;
  private List<Cursada4Prompt> cursadasAnteriores;
  private List<String> codigosMateriasInscriptasActuales;
}
