package com.edu.asistenteCupos.domain.filtros;

import com.edu.asistenteCupos.domain.peticion.PeticionInscripcion;

import java.util.List;

/**
 * Filtra las peticiones a comisiones que se superpongan en el horario con las inscripciones actuales
 */
public class FiltroSuperposicionHoraria implements FiltroDePeticionInscripcion {
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



