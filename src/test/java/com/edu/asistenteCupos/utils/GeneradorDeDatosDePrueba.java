package com.edu.asistenteCupos.utils;

import com.edu.asistenteCupos.domain.Estudiante;
import com.edu.asistenteCupos.domain.HistoriaAcademica;
import com.edu.asistenteCupos.domain.PeticionInscripcion;

import java.util.List;

public class GeneradorDeDatosDePrueba {
  public static List<PeticionInscripcion> peticionInscripcionesDePrueba() {
    Estudiante ana=Estudiante.builder().legajo("1001").nombre("Ana Torres").historiaAcademica(HistoriaAcademica.builder().insc3(2)
           .inscAct(0).aprobUlt(1).inscTot(6).aprobTot(3).restantes(5).correlativas("Si").build()).build();

    Estudiante carla=Estudiante.builder().legajo("1003").nombre("Carla Méndez").historiaAcademica(HistoriaAcademica.builder().insc3(1)
            .inscAct(1).aprobUlt(0).inscTot(4).aprobTot(2).restantes(10).correlativas("Si").build()).build();

    return List.of(new PeticionInscripcion(ana, "Matematica", List.of(""), true),
      new PeticionInscripcion(carla, "Matematica", List.of(""), true));
  }
}
