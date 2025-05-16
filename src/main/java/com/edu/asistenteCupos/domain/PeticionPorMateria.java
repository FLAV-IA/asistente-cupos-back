package com.edu.asistenteCupos.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
public class PeticionPorMateria {
  private List<Comision> comisiones;
  private boolean cumpleCorrelativa;

  public Materia getMateria() {
    if (comisiones == null || comisiones.isEmpty()) {
      throw new IllegalStateException("Petición sin comisiones asociadas");
    }
    return comisiones.get(0).getMateria();
  }

  public String getCddigoMateria() {
    return getMateria().getCodigo();
  }

  public Set<String> codigosDeComisiones() {
    return comisiones.stream().map(Comision::getCodigo).collect(Collectors.toSet());
  }
}
