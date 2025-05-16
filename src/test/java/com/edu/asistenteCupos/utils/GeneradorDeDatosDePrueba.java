package com.edu.asistenteCupos.utils;

import com.edu.asistenteCupos.service.factory.dto.HistoriaAcademica4Prompt;
import com.edu.asistenteCupos.service.factory.dto.PeticionInscripcion4Prompt;

import java.util.List;

public class GeneradorDeDatosDePrueba {
  public static List<PeticionInscripcion4Prompt> peticionInscripcionesDePrueba() {
    HistoriaAcademica4Prompt historiaAcademica4Prompt = HistoriaAcademica4Prompt.builder()
                                                                                .totalInscripcionesHistoricas(
                                                                                  "4")
                                                                                .totalHistoricasAprobadas(
                                                                                  "1").build();
    PeticionInscripcion4Prompt peticionInscripcion4Prompt = PeticionInscripcion4Prompt.builder()
                                                                                      .dni("1001")
                                                                                      .codigoMateria(
                                                                                        "1037")
                                                                                      .cumpleCorrelativa(
                                                                                        true)
                                                                                      .codigosComisionesPosibles(
                                                                                        List.of(
                                                                                          "1035",
                                                                                          "1036"))
                                                                                      .historiaAcademica(
                                                                                        historiaAcademica4Prompt)
                                                                                      .build();

    return List.of(peticionInscripcion4Prompt);
  }
}
