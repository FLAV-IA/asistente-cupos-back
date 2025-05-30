package com.edu.asistente_cupos.pipeline;

import com.edu.asistente_cupos.domain.priorizacion.PeticionPorMateriaPriorizada;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaInscripcion;
import com.edu.asistente_cupos.observacion.NombresMetricas;
import com.edu.asistente_cupos.observacion.TimeTracker;
import com.edu.asistente_cupos.service.asignacion.AsignadorDeCupos;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PasoAsignacion implements Paso<List<PeticionPorMateriaPriorizada>, List<SugerenciaInscripcion>> {
  private final AsignadorDeCupos asignador;
  private final TimeTracker timeTracker;

  @Override
  public List<SugerenciaInscripcion> ejecutar(List<PeticionPorMateriaPriorizada> peticiones) {
    return timeTracker.track(NombresMetricas.PASO_ASIGNACION_TOTAL,
      () -> asignador.asignar(peticiones));
  }
}
