package com.edu.asistente_cupos.service.sugerencia.manual;

import com.edu.asistente_cupos.domain.Comision;
import com.edu.asistente_cupos.domain.generacionDeSugerencias.SugerenciaExitosa;
import com.edu.asistente_cupos.domain.generacionDeSugerencias.SugerenciaFallida;
import com.edu.asistente_cupos.domain.priorizacion.PeticionPorMateriaPriorizada;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaInscripcion;
import com.edu.asistente_cupos.repository.ComisionRepository;
import com.edu.asistente_cupos.service.sugerencia.GeneradorDeSugerenciasDeCupos;
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
public class GeneradorDeSugerenciasDeCuposManual implements GeneradorDeSugerenciasDeCupos {
  private final ComisionRepository comisionRepository;

  @Override
  public List<SugerenciaInscripcion> sugerir(List<PeticionPorMateriaPriorizada> peticiones) {
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
        return new SugerenciaExitosa(comision).crearSugerencia(peticion.getEstudiante(),
          peticion.getMateria(), peticion.getMotivo(), peticion.getPrioridad());
      }
    }

    return new SugerenciaFallida(null).crearSugerencia(peticion.getEstudiante(),
      peticion.getMateria(), peticion.getMotivo(), peticion.getPrioridad());
  }
}
