package com.edu.asistente_cupos.domain.priorizacion;

import com.edu.asistente_cupos.domain.Comision;
import com.edu.asistente_cupos.domain.Estudiante;
import com.edu.asistente_cupos.domain.Materia;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
@Getter
public class PeticionPorMateriaPriorizada {
  private Estudiante estudiante;
  private Materia materia;
  private List<Comision> comisionesSolicitadas;
  private boolean cumpleCorrelativa;
  private int prioridad;
  private String motivo;

  public List<String> codigosDeComisiones() {
    return comisionesSolicitadas.stream().map(Comision::getCodigo).toList();
  }

  public boolean getCumpleCorrelativa() {
    return cumpleCorrelativa;
  }
}
