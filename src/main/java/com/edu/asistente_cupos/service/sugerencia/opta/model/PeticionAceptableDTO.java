package com.edu.asistente_cupos.service.sugerencia.opta.model;

import com.edu.asistente_cupos.domain.Estudiante;
import com.edu.asistente_cupos.domain.Materia;
import com.edu.asistente_cupos.domain.prompt.optimizado.HistoriaAcademica4Prompt;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import java.util.List;

@PlanningEntity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PeticionAceptableDTO {
  @PlanningId
  private String id;
  private String estudianteId;
  private String materiaCodigo;
  private List<String> codigosDeComisionPreferidas;
  private boolean cumpleCorrelativa;
  private int prioridad;
  private List<String> etiquetas;
  private HistoriaAcademica4Prompt historia;

  @ValueRangeProvider(id = "comisionesPosibles")
  private List<ComisionDTO> comisionesPosibles;
  @PlanningVariable(valueRangeProviderRefs = "comisionesPosibles", nullable = true)
  private ComisionDTO comisionSugerida;

  // NUEVOS CAMPOS para reconstruir dominio
  private Estudiante estudiante;
  private Materia materia;

}
