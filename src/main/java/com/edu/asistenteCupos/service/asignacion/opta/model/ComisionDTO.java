package com.edu.asistenteCupos.service.asignacion.opta.model;

import com.edu.asistenteCupos.domain.Comision;
import com.edu.asistenteCupos.domain.Materia;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.optaplanner.core.api.domain.lookup.PlanningId;

@EqualsAndHashCode(of = "codigo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComisionDTO {
  @PlanningId
  private String codigo;
  private String materiaCodigo;
  private String horario;
  private int cupo;

  public Comision toDomain(Materia materia) {
    return new Comision(this.codigo, this.horario, this.cupo, materia);
  }
}
