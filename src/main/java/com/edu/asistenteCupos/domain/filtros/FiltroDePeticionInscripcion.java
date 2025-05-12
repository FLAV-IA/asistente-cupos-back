package com.edu.asistenteCupos.domain.filtros;

import com.edu.asistenteCupos.domain.Comision;
import com.edu.asistenteCupos.domain.PeticionInscripcion;
import com.edu.asistenteCupos.domain.PeticionPorMateria;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public interface FiltroDePeticionInscripcion {
  void setFiltroSiguiente(FiltroDePeticionInscripcion siguiente);

  List<PeticionInscripcion> filtrar(List<PeticionInscripcion> peticiones);

  default List<PeticionInscripcion> filtrarPeticionesSegunPredicado(List<PeticionInscripcion> peticiones, BiPredicate<Comision, PeticionInscripcion> comisionPredicate) {
    List<PeticionInscripcion> filtradas = new ArrayList<>();

    for (PeticionInscripcion peticion : peticiones) {
      List<PeticionPorMateria> peticionPorMateriasFiltradas = new ArrayList<>();

      for (PeticionPorMateria ppm : peticion.getPeticionPorMaterias()) {
        List<Comision> comisionesFiltradas = ppm.getComisiones().stream()
                .filter(comision -> comisionPredicate.test(comision, peticion))
                .collect(Collectors.toList());

        if (!comisionesFiltradas.isEmpty()) {
          ppm.setComisiones(comisionesFiltradas);
          peticionPorMateriasFiltradas.add(ppm);
        }
      }

      if (!peticionPorMateriasFiltradas.isEmpty()) {
        peticion.setPeticionPorMaterias(peticionPorMateriasFiltradas);
        filtradas.add(peticion);
      }
    }
    return filtradas;
  }

}