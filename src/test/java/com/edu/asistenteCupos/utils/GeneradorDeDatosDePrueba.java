package com.edu.asistenteCupos.utils;

import com.edu.asistenteCupos.domain.Estudiante;
import com.edu.asistenteCupos.domain.HistoriaAcademica;
import com.edu.asistenteCupos.domain.PeticionInscripcion;

import java.util.List;

public class GeneradorDeDatosDePrueba {
  public static List<PeticionInscripcion> peticionInscripcionesDePrueba() {
    Estudiante ana=Estudiante.builder().legajo("1001").nombre("Ana Torres").historiaAcademica(HistoriaAcademica.builder().cantInscripciones3CursadasPrevias(2)
           .cantAprobadas3CursadasPrevias(1).cantInscripcionesHistoricas(6).cantMateriasAprobadasHistoricas(3).cantMateriasRestantes(5).cumpleCorrelatividad(true).build()).build();

    Estudiante carla=Estudiante.builder().legajo("1003").nombre("Carla Méndez").historiaAcademica(HistoriaAcademica.builder().cantInscripciones3CursadasPrevias(1)
           .cantAprobadas3CursadasPrevias(0).cantInscripcionesHistoricas(4).cantMateriasAprobadasHistoricas(2).cantMateriasRestantes(10).cumpleCorrelatividad(true).build()).build();

    return List.of(new PeticionInscripcion(ana, "Matematica", List.of(""), true),
      new PeticionInscripcion(carla, "Matematica", List.of(""), true));
  }
}
