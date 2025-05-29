package com.edu.asistente_cupos.pipeline;

import com.edu.asistente_cupos.domain.priorizacion.PeticionPorMateriaPriorizada;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaInscripcion;
import com.edu.asistente_cupos.service.asignacion.AsignadorDeCupos;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PasoAsignacion implements Paso<List<PeticionPorMateriaPriorizada>, List<SugerenciaInscripcion>> {
  private final AsignadorDeCupos asignador;

  @Override
  public List<SugerenciaInscripcion> ejecutar(List<PeticionPorMateriaPriorizada> input) {
    return asignador.asignar(input);
  }
}
