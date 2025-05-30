package com.edu.asistente_cupos.domain.filtros;

import com.edu.asistente_cupos.domain.peticion.PeticionInscripcion;

import java.util.List;
import java.util.function.Predicate;

/**
 * Filtra las peticiones a quienes están anotados a más de dos (configurable) materias.
 */
public class FiltrarAnotadosAVariasMaterias extends FiltroDePeticionInscripcion {
  private static final int MAX_MATERIAS = 2;

  @Override
  protected List<PeticionInscripcion> aplicarFiltro(List<PeticionInscripcion> peticiones) {
    Predicate<PeticionInscripcion> predicate = p -> !p.getEstudiante()
                                                      .estaInscriptoEnMasDe(MAX_MATERIAS);

    return filtrarPeticiones(peticiones, predicate);
  }
}
