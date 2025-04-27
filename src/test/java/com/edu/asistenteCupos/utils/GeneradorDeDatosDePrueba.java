package com.edu.asistenteCupos.utils;

import com.edu.asistenteCupos.domain.Estudiante;
import com.edu.asistenteCupos.domain.HistoriaAcademica;
import com.edu.asistenteCupos.domain.PeticionInscripcion;

import java.util.List;

public class GeneradorDeDatosDePrueba {
  public static List<PeticionInscripcion> peticionInscripcionesDePrueba() {
    Estudiante ana = Estudiante.builder().legajo("1001").nombre("Ana Torres").historiaAcademica(
      HistoriaAcademica.builder().totalInscripcionesHistoricas(6).build()).build();

    Estudiante carla = Estudiante.builder().legajo("1003").nombre("Carla Méndez").historiaAcademica(
      HistoriaAcademica.builder().totalInscripcionesHistoricas(4).build()).build();

    return List.of(new PeticionInscripcion(ana, "Matematica", List.of(), true),
      new PeticionInscripcion(carla, "Matematica", List.of(), true));
  }
}
