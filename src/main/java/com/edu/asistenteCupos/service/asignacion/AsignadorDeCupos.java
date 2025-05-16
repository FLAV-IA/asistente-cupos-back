package com.edu.asistenteCupos.service.asignacion;

import com.edu.asistenteCupos.domain.PeticionPriorizada;
import com.edu.asistenteCupos.domain.sugerencia.SugerenciaInscripcion;

import java.util.List;

public interface AsignadorDeCupos {
  List<SugerenciaInscripcion> asignar(List<PeticionPriorizada> priorizadas);
}