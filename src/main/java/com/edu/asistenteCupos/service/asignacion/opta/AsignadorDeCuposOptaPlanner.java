package com.edu.asistenteCupos.service.asignacion.opta;

import com.edu.asistenteCupos.domain.asignacion.AsignacionExitosa;
import com.edu.asistenteCupos.domain.asignacion.AsignacionFallida;
import com.edu.asistenteCupos.domain.priorizacion.PeticionPorMateriaPriorizada;
import com.edu.asistenteCupos.domain.sugerencia.SugerenciaInscripcion;
import com.edu.asistenteCupos.service.asignacion.AsignadorDeCupos;
import com.edu.asistenteCupos.service.asignacion.opta.model.AsignacionComisionesSolution;
import com.edu.asistenteCupos.service.asignacion.opta.model.ComisionDTO;
import com.edu.asistenteCupos.service.asignacion.opta.model.PeticionAsignableDTO;
import com.edu.asistenteCupos.service.asignacion.opta.reglas.ConfiguracionDeRestricciones;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.optaplanner.core.api.solver.SolverJob;
import org.optaplanner.core.api.solver.SolverManager;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Component
@Primary
@RequiredArgsConstructor
public class AsignadorDeCuposOptaPlanner implements AsignadorDeCupos {
  private final SolverManager<AsignacionComisionesSolution, Long> solverManager;
  @PostConstruct
  public void checkSolver() {
    System.out.println("OptaPlanner configurado correctamente");
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
                                                                        .peticiones(asignables).configuracion(new ConfiguracionDeRestricciones())
                                                                        .build();
    if (problema.getConfiguracion() == null) {
      throw new IllegalStateException("⚠️ No se configuró el ConstraintConfigurationProvider");
    }
    try {
      SolverJob<AsignacionComisionesSolution, Long> job = solverManager.solve(1L, problema);
      AsignacionComisionesSolution solucion = job.getFinalBestSolution();

      return solucion.getPeticiones().stream().map(this::reconstruirSugerencia).toList();

    } catch (InterruptedException | ExecutionException e) {
      throw new RuntimeException("Error al resolver el problema de asignación", e);
    }
  }


  private PeticionAsignableDTO convertirAPeticionAsignable(PeticionPorMateriaPriorizada peticion) {
    return PeticionAsignableDTO.builder().id(UUID.randomUUID().toString()).
            estudianteId(peticion.getEstudiante().getDni())
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
  private SugerenciaInscripcion reconstruirSugerencia(PeticionAsignableDTO dto) {
    if (dto.getComisionAsignada() == null) {
      return new AsignacionFallida().crearSugerencia(dto.getEstudiante(), dto.getMateria(),
        String.join(",", dto.getEtiquetas()), dto.getPrioridad());
    }

    return new AsignacionExitosa(
      dto.getComisionAsignada().toDomain(dto.getMateria())).crearSugerencia(dto.getEstudiante(),
      dto.getMateria(), String.join(",", dto.getEtiquetas()), dto.getPrioridad());
  }
}
