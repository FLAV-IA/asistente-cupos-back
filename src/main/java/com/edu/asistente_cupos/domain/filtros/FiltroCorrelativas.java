package com.edu.asistente_cupos.domain.filtros;

import com.edu.asistente_cupos.domain.peticion.PeticionInscripcion;

import java.util.List;

/**
 * Filtra las peticiones que no cumplen correlativas
 */
public class FiltroCorrelativas extends FiltroDePeticionInscripcion {
  @Override
  protected List<PeticionInscripcion> aplicarFiltro(List<PeticionInscripcion> peticiones) {
    return filtrarPeticionesPorComision(peticiones,
      peticion -> comision -> peticion.getEstudiante().puedeInscribirse(comision.getMateria()));
  }
}