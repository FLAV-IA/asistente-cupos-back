package com.edu.asistenteCupos.service.asignacion.opta.model;

import com.edu.asistenteCupos.service.asignacion.opta.reglas.ConfiguracionDeRestricciones;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.optaplanner.core.api.domain.constraintweight.ConstraintConfigurationProvider;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

import java.util.Arrays;
import java.util.List;

@PlanningSolution
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class AsignacionComisionesSolution {

  // 🔹 Las comisiones disponibles (hechos del problema)
  @ProblemFactCollectionProperty
  private List<ComisionDTO> comisiones;

  // 🔸 Las peticiones a resolver (entidades de planificación)
  @PlanningEntityCollectionProperty
  private List<PeticionAsignableDTO> peticiones;

  @PlanningScore
  public HardSoftScore getScore() {
    return score;
  }

  // ⭐ El score que OptaPlanner usa para evaluar la solución
  private HardSoftScore score;
  private ConfiguracionDeRestricciones configuracion;

  public AsignacionComisionesSolution(List<PeticionAsignableDTO> planningPeticiones, List<ComisionDTO> comisiones, ConfiguracionDeRestricciones configuracion) {
    this.peticiones = planningPeticiones;
    this.comisiones = comisiones;
    this.configuracion = configuracion;
  }

  @ConstraintConfigurationProvider
  public ConfiguracionDeRestricciones getConfiguracion() {
    return configuracion;
  }
  public void setScore(HardSoftScore score) {
    this.score = score;
  }

  public void setConfiguracion(ConfiguracionDeRestricciones configuracion) {
    this.configuracion = configuracion;
  }

  @ValueRangeProvider(id = "comisionesDisponibles")
  public List<ComisionDTO> getComisionesDisponibles() {
    return comisiones;
  }

  public List<PeticionAsignableDTO>  getPeticiones() {
    return peticiones;
  }
}
