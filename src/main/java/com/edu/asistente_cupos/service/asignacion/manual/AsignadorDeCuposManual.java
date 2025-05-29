package com.edu.asistente_cupos.service.asignacion.manual;

import com.edu.asistente_cupos.domain.Comision;
import com.edu.asistente_cupos.domain.asignacion.AsignacionExitosa;
import com.edu.asistente_cupos.domain.asignacion.AsignacionFallida;
import com.edu.asistente_cupos.domain.priorizacion.PeticionPorMateriaPriorizada;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaInscripcion;
import com.edu.asistente_cupos.repository.ComisionRepository;
import com.edu.asistente_cupos.service.asignacion.AsignadorDeCupos;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Asigna solo un cupo respetando la prioridad. El primero que llega, obtiene el primer cupo que pide.
 */
@Component
@RequiredArgsConstructor
public class AsignadorDeCuposManual implements AsignadorDeCupos {
  private final ComisionRepository comisionRepository;

  @Override
  public List<SugerenciaInscripcion> asignar(List<PeticionPorMateriaPriorizada> peticiones) {
    Set<String> codigos = peticiones.stream().flatMap(
                                      p -> p.getComisionesSolicitadas().stream().map(Comision::getCodigo))
                                    .collect(Collectors.toSet());

    List<Comision> comisiones = comisionRepository.findByCodigoIn(codigos);
    GestorDeCupos gestor = new GestorDeCupos(comisiones);

    return peticiones.stream().sorted(
                       Comparator.comparingInt(PeticionPorMateriaPriorizada::getPrioridad).reversed())
                     .map(peticion -> asignarPeticion(peticion, gestor)).toList();
  }

  private SugerenciaInscripcion asignarPeticion(PeticionPorMateriaPriorizada peticion, GestorDeCupos gestor) {
    for (Comision comision : peticion.getComisionesSolicitadas()) {
      if (gestor.asignarCupo(comision.getCodigo())) {
        return new AsignacionExitosa(comision).crearSugerencia(peticion.getEstudiante(),
          peticion.getMateria(), peticion.getMotivo(), peticion.getPrioridad());
      }
    }

    return new AsignacionFallida(null).crearSugerencia(peticion.getEstudiante(), peticion.getMateria(),
      peticion.getMotivo(), peticion.getPrioridad());
  }
}
