package com.edu.asistente_cupos.domain.filtros;

import com.edu.asistente_cupos.domain.Comision;
import com.edu.asistente_cupos.domain.peticion.PeticionInscripcion;
import com.edu.asistente_cupos.domain.peticion.PeticionPorMateria;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public interface FiltroDePeticionInscripcion {
  void setFiltroSiguiente(FiltroDePeticionInscripcion siguiente);

  List<PeticionInscripcion> filtrar(List<PeticionInscripcion> peticiones);

  default List<PeticionInscripcion> filtrarPeticionesSegunPredicado(List<PeticionInscripcion> peticiones, BiPredicate<Comision, PeticionInscripcion> comisionPredicate) {
    List<PeticionInscripcion> filtradas = new ArrayList<>();

    for (PeticionInscripcion peticion : peticiones) {
      List<PeticionPorMateria> peticionPorMateriasFiltradas = new ArrayList<>();

      for (PeticionPorMateria ppm : peticion.getPeticionPorMaterias()) {
        List<Comision> comisionesFiltradas = ppm.getComisiones().stream().filter(
          comision -> comisionPredicate.test(comision, peticion)).collect(Collectors.toList());

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