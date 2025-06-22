package com.edu.asistente_cupos.service.sugerencia;

import com.edu.asistente_cupos.domain.priorizacion.PeticionPorMateriaPriorizada;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaInscripcion;

import java.util.List;

public interface GeneradorDeSugerenciasDeCupos {
  List<SugerenciaInscripcion> sugerir(List<PeticionPorMateriaPriorizada> priorizadas);
}