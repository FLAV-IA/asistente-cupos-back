package com.edu.asistenteCupos.domain.filtros;

import com.edu.asistenteCupos.domain.Comision;
import com.edu.asistenteCupos.domain.peticion.PeticionInscripcion;

import java.util.List;
import java.util.function.BiPredicate;

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
    BiPredicate<Comision, PeticionInscripcion> comisionPredicate = (c, p) ->  p.getEstudiante().getHistoriaAcademica().getInscripcionesActuales().size()<3;

    List<PeticionInscripcion> filtradas =filtrarPeticionesSegunPredicado(peticiones, comisionPredicate);
    if (siguiente != null) {
      return siguiente.filtrar(filtradas);
    }
    return filtradas;
  }
}



