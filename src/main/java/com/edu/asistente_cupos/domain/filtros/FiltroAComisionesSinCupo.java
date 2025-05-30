package com.edu.asistente_cupos.domain.filtros;

import com.edu.asistente_cupos.domain.peticion.PeticionInscripcion;

import java.util.List;

/**
 * Filtra las peticiones a comisiones sin cupo
 */
public class FiltroAComisionesSinCupo implements FiltroDePeticionInscripcion {
  private FiltroDePeticionInscripcion siguiente;


  @Override
  public void setFiltroSiguiente(FiltroDePeticionInscripcion siguiente) {
    this.siguiente = siguiente;
  }

  @Override
  public List<PeticionInscripcion> filtrar(List<PeticionInscripcion> peticiones) {
    List<PeticionInscripcion> filtradas = filtrarPeticionesSegunPredicado(peticiones,
      (comisionAEvaluar, peticionInscripcionAEvaluar) -> comisionAEvaluar.getCupo() > 0);

    if (siguiente != null) {
      return siguiente.filtrar(filtradas);
    }

    return filtradas;
  }
}