package com.edu.asistenteCupos.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@Builder
public class PeticionInscripcion {
  private Estudiante estudiante;
  private List<PeticionPorMateria> peticionPorMaterias;

  public Set<String> codigosDeComisionesSolicitadas() {
    return peticionPorMaterias.stream()
                              .flatMap(pm -> pm.codigosDeComisiones().stream())
                              .collect(Collectors.toSet());
  }
}
