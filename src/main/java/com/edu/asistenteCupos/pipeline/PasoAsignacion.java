package com.edu.asistenteCupos.pipeline;

import com.edu.asistenteCupos.domain.priorizacion.PeticionPorMateriaPriorizada;
import com.edu.asistenteCupos.domain.sugerencia.SugerenciaInscripcion;
import com.edu.asistenteCupos.service.asignacion.AsignadorDeCupos;
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
