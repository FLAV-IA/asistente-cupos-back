package com.edu.asistenteCupos.domain.filtros;

import com.edu.asistenteCupos.domain.PeticionInscripcion;

import java.util.List;

/**
 * Filtra las peticiones a quienes están anotados a más de dos(configurable) materias.
 */
public class FiltrarAnotadosAVariasMaterias implements FiltroDePeticionInscripcion {
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



