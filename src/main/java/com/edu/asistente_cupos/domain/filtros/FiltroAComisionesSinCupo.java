package com.edu.asistente_cupos.domain.filtros;

import com.edu.asistente_cupos.domain.Comision;
import com.edu.asistente_cupos.domain.peticion.PeticionInscripcion;

import java.util.List;

/**
 * Filtra las peticiones a comisiones sin cupo
 */
public class FiltroAComisionesSinCupo extends FiltroDePeticionInscripcion {
  @Override
  protected List<PeticionInscripcion> aplicarFiltro(List<PeticionInscripcion> peticiones) {
    return filtrarPeticionesPorComision(peticiones, peticion -> Comision::tieneCupo);
  }
}
