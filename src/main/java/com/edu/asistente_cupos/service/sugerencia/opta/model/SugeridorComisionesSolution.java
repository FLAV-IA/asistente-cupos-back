package com.edu.asistente_cupos.service.sugerencia.opta.model;

import com.edu.asistente_cupos.service.sugerencia.opta.reglas.ConfiguracionDeRestricciones;
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

import java.util.List;

@PlanningSolution
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class SugeridorComisionesSolution {
  @ValueRangeProvider(id = "comisionesDisponibles")
  @ProblemFactCollectionProperty
  private List<ComisionDTO> comisiones;

  @PlanningEntityCollectionProperty
  private List<PeticionAceptableDTO> peticiones;

  @PlanningScore
  private HardSoftScore score;

  @ConstraintConfigurationProvider
  private ConfiguracionDeRestricciones configuracion;
}
