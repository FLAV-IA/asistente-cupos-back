package com.edu.asistenteCupos.domain.filtros;

import com.edu.asistenteCupos.domain.peticion.PeticionInscripcion;

import java.util.List;

/**
 * Filtra las peticiones que no cumplen correlativas y adeuda >2 materias para finalizar la carrera
 */
public class FiltroCorrelativas implements FiltroDePeticionInscripcion {
  private FiltroDePeticionInscripcion siguiente;

  @Override
  public void setFiltroSiguiente(FiltroDePeticionInscripcion siguiente) {
    this.siguiente = siguiente;
  }

  @Override
  public List<PeticionInscripcion> filtrar(List<PeticionInscripcion> peticiones) {
    if (siguiente != null) {
      return siguiente.filtrar(peticiones);
    }
    return peticiones;
  }
}



