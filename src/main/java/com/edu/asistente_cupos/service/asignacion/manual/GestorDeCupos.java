package com.edu.asistente_cupos.service.asignacion.manual;

import com.edu.asistente_cupos.domain.Comision;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GestorDeCupos {
  private final Map<String, Integer> cuposPorComision;

  public GestorDeCupos(List<Comision> comisiones) {
    this.cuposPorComision = comisiones.stream().collect(
      Collectors.toMap(Comision::getCodigo, Comision::getCupo));
  }

  public boolean asignarCupo(String codigoComision) {
    int cupo = cuposPorComision.getOrDefault(codigoComision, 0);
    if (cupo <= 0)
      return false;
    cuposPorComision.put(codigoComision, cupo - 1);
    return true;
  }
}
