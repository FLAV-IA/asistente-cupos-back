package com.edu.asistente_cupos.service.asignacion;

import com.edu.asistente_cupos.domain.priorizacion.PeticionPorMateriaPriorizada;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaInscripcion;

import java.util.List;

public interface AsignadorDeCupos {
  List<SugerenciaInscripcion> asignar(List<PeticionPorMateriaPriorizada> priorizadas);
}