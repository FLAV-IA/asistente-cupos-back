package com.edu.asistenteCupos.service.asignacion.opta.model;

import com.edu.asistenteCupos.domain.Estudiante;
import com.edu.asistenteCupos.domain.Materia;
import com.edu.asistenteCupos.domain.prompt.optimizado.HistoriaAcademica4Prompt;
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
public class PeticionAsignableDTO {
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
  @PlanningVariable(valueRangeProviderRefs = "comisionesPosibles",nullable = true)
  private ComisionDTO comisionAsignada;

  // NUEVOS CAMPOS para reconstruir dominio
  private Estudiante estudiante;
  private Materia materia;

}
