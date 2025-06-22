package com.edu.asistente_cupos.pipeline;

import com.edu.asistente_cupos.domain.priorizacion.PeticionPorMateriaPriorizada;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaInscripcion;
import com.edu.asistente_cupos.observacion.NombresMetricas;
import com.edu.asistente_cupos.observacion.TimeTracker;
import com.edu.asistente_cupos.service.sugerencia.GeneradorDeSugerenciasDeCupos;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PasoGeneracionDeSugerencia implements Paso<List<PeticionPorMateriaPriorizada>, List<SugerenciaInscripcion>> {
  private final GeneradorDeSugerenciasDeCupos asignador;
  private final TimeTracker timeTracker;

  @Override
  public List<SugerenciaInscripcion> ejecutar(List<PeticionPorMateriaPriorizada> peticiones) {
    return timeTracker.track(NombresMetricas.PASO_ASIGNACION_TOTAL,
      () -> asignador.sugerir(peticiones));
  }
}
