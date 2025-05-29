package com.edu.asistente_cupos.service.asignacion.opta;

import com.edu.asistente_cupos.domain.asignacion.AsignacionExitosa;
import com.edu.asistente_cupos.domain.asignacion.AsignacionFallida;
import com.edu.asistente_cupos.domain.priorizacion.PeticionPorMateriaPriorizada;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaInscripcion;
import com.edu.asistente_cupos.excepcion.opta.AsignacionRuntimeException;
import com.edu.asistente_cupos.excepcion.opta.ConfiguracionDeAsignacionInvalidaException;
import com.edu.asistente_cupos.excepcion.opta.ErrorDeEjecucionDeAsignacionException;
import com.edu.asistente_cupos.excepcion.opta.InterrupcionDuranteAsignacionException;
import com.edu.asistente_cupos.service.asignacion.AsignadorDeCupos;
import com.edu.asistente_cupos.service.asignacion.opta.model.AsignacionComisionesSolution;
import com.edu.asistente_cupos.service.asignacion.opta.model.ComisionDTO;
import com.edu.asistente_cupos.service.asignacion.opta.model.PeticionAsignableDTO;
import com.edu.asistente_cupos.service.asignacion.opta.reglas.ConfiguracionDeRestricciones;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.optaplanner.core.api.solver.SolverJob;
import org.optaplanner.core.api.solver.SolverManager;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Component
@Primary
@RequiredArgsConstructor
@Slf4j
public class AsignadorDeCuposOptaPlanner implements AsignadorDeCupos {
  private final SolverManager<AsignacionComisionesSolution, Long> solverManager;

  @PostConstruct
  public void checkSolver() {
    log.info("OptaPlanner configurado correctamente");
  }

  @Override
  public List<SugerenciaInscripcion> asignar(List<PeticionPorMateriaPriorizada> peticiones) {
    List<PeticionAsignableDTO> asignables = peticiones.stream()
                                                      .map(this::convertirAPeticionAsignable)
                                                      .toList();

    List<ComisionDTO> comisiones = asignables.stream()
                                             .flatMap(p -> p.getComisionesPosibles().stream())
                                             .distinct().toList();

    AsignacionComisionesSolution problema = AsignacionComisionesSolution.builder()
                                                                        .comisiones(comisiones)
                                                                        .peticiones(asignables)
                                                                        .configuracion(
                                                                          new ConfiguracionDeRestricciones())
                                                                        .build();
    if (problema.getConfiguracion() == null) {
      throw new ConfiguracionDeAsignacionInvalidaException(
        "Falta la configuración de restricciones");
    }
    try {
      SolverJob<AsignacionComisionesSolution, Long> job = solverManager.solve(1L, problema);
      AsignacionComisionesSolution solucion = job.getFinalBestSolution();

      return solucion.getPeticiones().stream().flatMap(p -> {
        List<SugerenciaInscripcion> sugerencias = reconstruirSugerencia(p);
        return sugerencias.stream();
      }).toList();

    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new AsignacionRuntimeException(new InterrupcionDuranteAsignacionException(e));

    } catch (ExecutionException e) {
      throw new AsignacionRuntimeException(new ErrorDeEjecucionDeAsignacionException(e));
    }
  }

  private PeticionAsignableDTO convertirAPeticionAsignable(PeticionPorMateriaPriorizada peticion) {
    return PeticionAsignableDTO.builder().id(UUID.randomUUID().toString())
                               .estudianteId(peticion.getEstudiante().getDni())
                               .materiaCodigo(peticion.getMateria().getCodigo())
                               .codigosDeComisionPreferidas(peticion.codigosDeComisiones())
                               .cumpleCorrelativa(peticion.getCumpleCorrelativa())
                               .prioridad(peticion.getPrioridad())
                               .etiquetas(Arrays.asList(peticion.getMotivo().split(",")))
                               .comisionesPosibles(peticion.getComisionesSolicitadas().stream().map(
                                 c -> new ComisionDTO(c.getCodigo(), c.getMateria().getCodigo(),
                                   c.getHorario(), c.getCupo())).toList())
                               .estudiante(peticion.getEstudiante()).materia(peticion.getMateria())
                               .build();
  }

  private List<SugerenciaInscripcion> reconstruirSugerencia(PeticionAsignableDTO dto) {
    ComisionDTO comisionAsignada = dto.getComisionAsignada();

    List<SugerenciaInscripcion> sugerenciasFallidas = dto.getComisionesPosibles().stream().filter(
      c -> !Objects.equals(c, comisionAsignada)).map(
      c -> new AsignacionFallida(c.toDomain(dto.getMateria())).crearSugerencia(dto.getEstudiante(),
        dto.getMateria(), String.join(",", dto.getEtiquetas()), dto.getPrioridad())).toList();

    List<SugerenciaInscripcion> sugerenciasProcesadas = new ArrayList<>();

    if (comisionAsignada != null) {
      SugerenciaInscripcion sugerenciaExitosa = new AsignacionExitosa(
        comisionAsignada.toDomain(dto.getMateria())).crearSugerencia(dto.getEstudiante(),
        dto.getMateria(), String.join(",", dto.getEtiquetas()), dto.getPrioridad());
      sugerenciasProcesadas.add(sugerenciaExitosa);
    }

    sugerenciasProcesadas.addAll(sugerenciasFallidas);
    return sugerenciasProcesadas;
  }
}