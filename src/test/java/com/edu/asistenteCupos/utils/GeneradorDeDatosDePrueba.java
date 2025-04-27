package com.edu.asistenteCupos.utils;

import com.edu.asistenteCupos.domain.Estudiante;
import com.edu.asistenteCupos.domain.HistoriaAcademica;
import com.edu.asistenteCupos.domain.Materia;
import com.edu.asistenteCupos.domain.PeticionInscripcion;

import java.util.List;

public class GeneradorDeDatosDePrueba {
  public static List<PeticionInscripcion> peticionInscripcionesDePrueba() {
    Estudiante ana = Estudiante.builder().dni("1001").nombre("Ana Torres").historiaAcademica(
      HistoriaAcademica.builder().totalInscripcionesHistoricas(6).build()).build();

    Estudiante carla = Estudiante.builder().dni("1003").nombre("Carla Méndez").historiaAcademica(
      HistoriaAcademica.builder().totalInscripcionesHistoricas(4).build()).build();

    return List.of(new PeticionInscripcion(ana, new Materia(), List.of(), true),
      new PeticionInscripcion(carla, new Materia(), List.of(), true));
  }
}
