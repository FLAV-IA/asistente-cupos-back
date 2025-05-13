package com.edu.asistenteCupos.domain.filtros;

import com.edu.asistenteCupos.domain.PeticionInscripcion;
import com.edu.asistenteCupos.repository.ComisionRepository;

import java.util.ArrayList;
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
    // chequear si solamente se debe reemplazar por  comisionAEvaluar.getCupo() > 0
    List<PeticionInscripcion> filtradas =filtrarPeticionesSegunPredicado(peticiones, (comisionAEvaluar,peticionInscripcionAEvaluar) -> comisionAEvaluar.getCupo() > 0);

    if (siguiente != null) {
      return siguiente.filtrar(filtradas);
    }

    return filtradas;
  }


}
