package com.edu.asistenteCupos.service.asignacion;

import com.edu.asistenteCupos.domain.priorizacion.PeticionPorMateriaPriorizada;
import com.edu.asistenteCupos.domain.sugerencia.SugerenciaInscripcion;

import java.util.List;

public interface AsignadorDeCupos {
  List<SugerenciaInscripcion> asignar(List<PeticionPorMateriaPriorizada> priorizadas);
}