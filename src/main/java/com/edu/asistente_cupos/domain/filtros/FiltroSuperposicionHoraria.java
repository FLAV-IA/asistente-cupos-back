package com.edu.asistente_cupos.domain.filtros;

import com.edu.asistente_cupos.domain.peticion.PeticionInscripcion;

import java.util.List;

/**
 * Filtra las peticiones a comisiones que se superpongan en el horario con las inscripciones actuales.
 */
public class FiltroSuperposicionHoraria extends FiltroDePeticionInscripcion {

  @Override
  protected List<PeticionInscripcion> aplicarFiltro(List<PeticionInscripcion> peticiones) {
    return filtrarPeticionesPorComision(peticiones,
      peticion -> comision -> !peticion.getEstudiante().getHistoriaAcademica()
                                       .haySuperposicionHoraria(comision));
  }
}
