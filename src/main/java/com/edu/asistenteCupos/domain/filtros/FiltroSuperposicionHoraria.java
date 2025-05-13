package com.edu.asistenteCupos.domain.filtros;

import com.edu.asistenteCupos.domain.Comision;
import com.edu.asistenteCupos.domain.PeticionInscripcion;

import java.util.List;
import java.util.function.BiPredicate;

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
    BiPredicate<Comision, PeticionInscripcion> comisionPredicate = (c, p) ->  p.getEstudiante().getHistoriaAcademica().haySuperposicionHoraria(c);

    List<PeticionInscripcion> filtradas =filtrarPeticionesSegunPredicado(peticiones, comisionPredicate);


    if (siguiente != null) {
      return siguiente.filtrar(filtradas);
    }
    return filtradas;
  }
}



