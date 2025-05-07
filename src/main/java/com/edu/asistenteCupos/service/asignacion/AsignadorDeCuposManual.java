package com.edu.asistenteCupos.service.asignacion;

import com.edu.asistenteCupos.domain.Comision;
import com.edu.asistenteCupos.domain.PeticionPriorizada;
import com.edu.asistenteCupos.repository.ComisionRepository;
import com.edu.asistenteCupos.domain.sugerencia.SugerenciaInscripcion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Asigna solo un cupo respetando la prioridad. El primero que llega, obtiene el primer cupo que pide.
 */
@Component
@RequiredArgsConstructor
public class AsignadorDeCuposManual implements AsignadorDeCupos {

  private final ComisionRepository comisionRepository;
  private final AsignadorPorMateria asignadorPorMateria = new AsignadorPorMateria();

  public List<SugerenciaInscripcion> asignar(List<PeticionPriorizada> priorizadas) {
    Set<String> codigos = priorizadas.stream()
                                     .flatMap(p -> p.codigosComisionesSolicitadas().stream())
                                     .collect(Collectors.toSet());

    Map<String, Integer> cupos = comisionRepository.findByCodigoIn(codigos).stream().collect(
      Collectors.toMap(Comision::getCodigo, Comision::getCupo));

    return priorizadas.stream()
                      .sorted(Comparator.comparingInt(PeticionPriorizada::getPrioridad).reversed())
                      .flatMap(p -> p.generarSugerencias(cupos, asignadorPorMateria).stream())
                      .toList();

  }
}

