package com.edu.asistenteCupos.service.asignacion;

import com.edu.asistenteCupos.domain.asignacion.AsignacionExitosa;
import com.edu.asistenteCupos.domain.asignacion.AsignacionFallida;
import com.edu.asistenteCupos.domain.Comision;
import com.edu.asistenteCupos.domain.PeticionPorMateria;
import com.edu.asistenteCupos.domain.asignacion.ResultadoAsignacion;

import java.util.Map;

public class AsignadorPorMateria {
  public ResultadoAsignacion asignar(PeticionPorMateria peticion, Map<String, Integer> cuposDisponibles) {
    for (Comision comision : peticion.getComisiones()) {
      int cupo = cuposDisponibles.getOrDefault(comision.getCodigo(), 0);
      if (cupo > 0) {
        cuposDisponibles.put(comision.getCodigo(), cupo - 1);
        return new AsignacionExitosa(comision);
      }
    }
    return new AsignacionFallida();
  }
}