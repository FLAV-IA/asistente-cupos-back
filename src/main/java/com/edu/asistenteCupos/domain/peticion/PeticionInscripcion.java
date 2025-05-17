package com.edu.asistenteCupos.domain.peticion;

import com.edu.asistenteCupos.domain.Comision;
import com.edu.asistenteCupos.domain.Estudiante;
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
    return peticionPorMaterias.stream().flatMap(pm -> pm.codigosDeComisiones().stream())
                              .collect(Collectors.toSet());
  }

  public List<Comision> obtenerComisionesPara(String codigoMateria) {
    return obtenerPeticionxMateriaPor(codigoMateria).getComisiones();
  }

  public boolean cumpleCorrelativa(String codigoMateria) {
    return obtenerPeticionxMateriaPor(codigoMateria).isCumpleCorrelativa();
  }

  private PeticionPorMateria obtenerPeticionxMateriaPor(String codigoMateria) {
    return peticionPorMaterias.stream().filter(p -> p.perteneceAMateria(codigoMateria)).findFirst()
                              .orElseThrow(() -> new IllegalArgumentException(
                                "No se encontró una petición para la materia con código: " +
                                  codigoMateria));
  }
}
