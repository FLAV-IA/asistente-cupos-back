package com.edu.asistenteCupos.domain.filtros;

import com.edu.asistenteCupos.domain.Comision;
import com.edu.asistenteCupos.domain.PeticionInscripcion;
import com.edu.asistenteCupos.domain.PeticionPorMateria;
import com.edu.asistenteCupos.repository.ComisionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Filtra las peticiones a comisiones sin cupo
 */
public class FiltroAComisionesSinCupo implements FiltroDePeticionInscripcion {
  private final ComisionRepository comisionRepository;
  private FiltroDePeticionInscripcion siguiente;
  public FiltroAComisionesSinCupo(ComisionRepository comisionRepository) {
    this.comisionRepository = comisionRepository;
  }

  @Override
  public void setFiltroSiguiente(FiltroDePeticionInscripcion siguiente) {
    this.siguiente = siguiente;
  }

  @Override
  public List<PeticionInscripcion> filtrar(List<PeticionInscripcion> peticiones) {
    List<PeticionInscripcion> filtradas = new ArrayList<>();

    for (PeticionInscripcion peticion : peticiones) {
      List<PeticionPorMateria> materiasConCupo = new ArrayList<>();

      for (PeticionPorMateria ppm : peticion.getPeticionPorMaterias()) {
        List<Comision> comisionesConCupo = ppm.getComisiones().stream()
                .filter(c -> comisionRepository.findCupoByCodigo(c.getCodigo()) > 0)
                .collect(Collectors.toList());

        if (!comisionesConCupo.isEmpty()) {
          ppm.setComisiones(comisionesConCupo);
          materiasConCupo.add(ppm);
        }
      }

      if (!materiasConCupo.isEmpty()) {
        peticion.setPeticionPorMaterias(materiasConCupo);
        filtradas.add(peticion);
      }
    }

    if (siguiente != null) {
      return siguiente.filtrar(filtradas);
    }

    return filtradas;
  }
}
