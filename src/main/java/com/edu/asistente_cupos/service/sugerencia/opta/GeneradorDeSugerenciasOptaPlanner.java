package com.edu.asistente_cupos.service.sugerencia.opta;

import com.edu.asistente_cupos.domain.generacionDeSugerencias.SugerenciaExitosa;
import com.edu.asistente_cupos.domain.generacionDeSugerencias.SugerenciaFallida;
import com.edu.asistente_cupos.domain.priorizacion.PeticionPorMateriaPriorizada;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaInscripcion;
import com.edu.asistente_cupos.excepcion.opta.ConfiguracionDeGeneracionDeSugerenciaInvalidaException;
import com.edu.asistente_cupos.observacion.NombresMetricas;
import com.edu.asistente_cupos.observacion.TimeTracker;
import com.edu.asistente_cupos.service.sugerencia.GeneradorDeSugerenciasDeCupos;
import com.edu.asistente_cupos.service.sugerencia.opta.model.SugeridorComisionesSolution;
import com.edu.asistente_cupos.service.sugerencia.opta.model.ComisionDTO;
import com.edu.asistente_cupos.service.sugerencia.opta.model.PeticionAceptableDTO;
import com.edu.asistente_cupos.service.sugerencia.opta.reglas.ConfiguracionDeRestricciones;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.solver.SolverJob;
import org.optaplanner.core.api.solver.SolverManager;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
@Primary
@RequiredArgsConstructor
@Slf4j
public class GeneradorDeSugerenciasOptaPlanner implements GeneradorDeSugerenciasDeCupos {
  private final SolverManager<SugeridorComisionesSolution, Long> solverManager;
  private final TimeTracker timeTracker;
  private final MeterRegistry meterRegistry;

  @PostConstruct
  public void checkSolver() {
    log.info("OptaPlanner configurado correctamente");
  }

  @Override
  public List<SugerenciaInscripcion> sugerir(List<PeticionPorMateriaPriorizada> peticiones) {
    List<PeticionAceptableDTO> asignables = peticiones.stream()
                                                      .map(this::convertirAPeticionAsignable)
                                                      .toList();

    List<ComisionDTO> comisiones = asignables.stream()
                                             .flatMap(p -> p.getComisionesPosibles().stream())
                                             .distinct().toList();

    SugeridorComisionesSolution problema = SugeridorComisionesSolution.builder()
                                                                        .comisiones(comisiones)
                                                                        .peticiones(asignables)
                                                                        .configuracion(
                                                                          new ConfiguracionDeRestricciones())
                                                                        .build();

    if (problema.getConfiguracion() == null) {
      throw new ConfiguracionDeGeneracionDeSugerenciaInvalidaException(
        "Falta la configuración de restricciones");
    }

    SugeridorComisionesSolution solucion = timeTracker.track(NombresMetricas.OPTA_SOLUCION, () -> {
      SolverJob<SugeridorComisionesSolution, Long> job = solverManager.solve(1L, problema);
      return job.getFinalBestSolution();
    });

    HardSoftScore score = solucion.getScore();
    log.info("Score final: {}", score);

    meterRegistry.gauge(NombresMetricas.OPTA_SCORE_HARD, score.hardScore());
    meterRegistry.gauge(NombresMetricas.OPTA_SCORE_SOFT, score.softScore());

    long asignadas = solucion.getPeticiones().stream().filter(p -> p.getComisionSugerida() != null)
                             .count();

    meterRegistry.gauge(NombresMetricas.OPTA_PETICIONES_ASIGNADAS, asignadas);

    return solucion.getPeticiones().stream().flatMap(p -> reconstruirSugerencia(p).stream())
                   .toList();

  }

  private PeticionAceptableDTO convertirAPeticionAsignable(PeticionPorMateriaPriorizada peticion) {
    return PeticionAceptableDTO.builder().id(UUID.randomUUID().toString())
                               .estudianteId(peticion.getEstudiante().getDni())
                               .materiaCodigo(peticion.getMateria().getCodigo())
                               .codigosDeComisionPreferidas(peticion.codigosDeComisiones())
                               .cumpleCorrelativa(peticion.getCumpleCorrelativa())
                               .prioridad(peticion.getPrioridad())
                               .etiquetas(Arrays.asList(peticion.getMotivo().split(",")))
                               .comisionesPosibles(peticion.getComisionesSolicitadas().stream().map(
                                 c -> new ComisionDTO(c.getCodigo(), c.getMateria().getCodigo(),
                                   c.getHorario().toString(), c.getCupo())).toList())
                               .estudiante(peticion.getEstudiante()).materia(peticion.getMateria())
                               .build();
  }

  private List<SugerenciaInscripcion> reconstruirSugerencia(PeticionAceptableDTO dto) {
    ComisionDTO comisionAsignada = dto.getComisionSugerida();

    List<SugerenciaInscripcion> sugerenciasFallidas = dto.getComisionesPosibles().stream().filter(
      c -> !Objects.equals(c, comisionAsignada)).map(
      c -> new SugerenciaFallida(c.toDomain(dto.getMateria())).crearSugerencia(dto.getEstudiante(),
        dto.getMateria(), String.join(",", dto.getEtiquetas()), dto.getPrioridad())).toList();

    List<SugerenciaInscripcion> sugerenciasProcesadas = new ArrayList<>();

    if (comisionAsignada != null) {
      SugerenciaInscripcion sugerenciaExitosa = new SugerenciaExitosa(
        comisionAsignada.toDomain(dto.getMateria())).crearSugerencia(dto.getEstudiante(),
        dto.getMateria(), String.join(",", dto.getEtiquetas()), dto.getPrioridad());
      sugerenciasProcesadas.add(sugerenciaExitosa);
    }

    sugerenciasProcesadas.addAll(sugerenciasFallidas);
    return sugerenciasProcesadas;
  }
}