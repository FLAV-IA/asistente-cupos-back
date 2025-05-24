package com.edu.asistenteCupos.domain.priorizacion;

import com.edu.asistenteCupos.domain.Comision;
import com.edu.asistenteCupos.domain.Estudiante;
import com.edu.asistenteCupos.domain.Materia;
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
    return comisionesSolicitadas.stream()
                                .map(Comision::getCodigo)
                                .toList();
  }

  public boolean getCumpleCorrelativa() {
    return cumpleCorrelativa;
  }
}
