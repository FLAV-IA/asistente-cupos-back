package com.edu.asistente_cupos.controller.dto;

import lombok.Builder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
public record PeticionInscripcionDTO(String nombre, String dni,
                                     HistoriaAcademicaDTO historiaAcademica,
                                     List<PeticionPorMateriaDTO> materias) {
  public Set<String> comisionesSolicitadas() {
    Set<String> codigos = new HashSet<>();
    for (PeticionPorMateriaDTO m : materias) {
      codigos.addAll(m.codigosComisionesSolicitadas());
    }
    return codigos;
  }
}