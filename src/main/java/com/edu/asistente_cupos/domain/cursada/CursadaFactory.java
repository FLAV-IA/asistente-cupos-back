package com.edu.asistente_cupos.domain.cursada;

import com.edu.asistente_cupos.domain.Materia;

public class CursadaFactory {
  public static Cursada aprobada(Materia materia, int nota) {
    return Cursada.builder().materia(materia).estado(new EstadoCursadaAprobada(nota)).build();
  }

  public static Cursada desaprobada(Materia materia, int nota) {
    return Cursada.builder().materia(materia).estado(new EstadoCursadaDesaprobada(nota)).build();
  }

  public static Cursada enCurso(Materia materia) {
    return Cursada.builder().materia(materia).estado(new EstadoCursadaEnCurso()).build();
  }
}
