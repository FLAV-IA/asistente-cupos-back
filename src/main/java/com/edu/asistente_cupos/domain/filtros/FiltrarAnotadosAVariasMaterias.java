package com.edu.asistente_cupos.domain.filtros;

import com.edu.asistente_cupos.domain.peticion.PeticionInscripcion;

import java.util.List;
import java.util.function.Predicate;

/**
 * Filtra las peticiones a quienes están anotados a más de dos (configurable) materias.
 */
public class FiltrarAnotadosAVariasMaterias implements FiltroDePeticionInscripcion {
  private static final int MAX_MATERIAS = 2;
  private FiltroDePeticionInscripcion siguiente;

  @Override
  public void setFiltroSiguiente(FiltroDePeticionInscripcion siguiente) {
    this.siguiente = siguiente;
  }

  @Override
  public List<PeticionInscripcion> filtrar(List<PeticionInscripcion> peticiones) {
    Predicate<PeticionInscripcion> predicate = p -> !p.getEstudiante()
                                                     .estaInscriptoEnMasDe(MAX_MATERIAS);

    List<PeticionInscripcion> filtradas = filtrarPeticiones(peticiones, predicate);

    if (siguiente != null) {
      return siguiente.filtrar(filtradas);
    }
    return filtradas;
  }
}
