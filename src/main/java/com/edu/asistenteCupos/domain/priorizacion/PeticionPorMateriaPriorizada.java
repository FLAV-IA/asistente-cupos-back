package com.edu.asistenteCupos.domain.priorizacion;

import com.edu.asistenteCupos.domain.Comision;
import com.edu.asistenteCupos.domain.Estudiante;
import com.edu.asistenteCupos.domain.Materia;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PeticionPorMateriaPriorizada {
  private Estudiante estudiante;
  private Materia materia;
  private List<Comision> comisionesSolicitadas;
  private boolean cumpleCorrelativa;
  private int prioridad;
  private String motivo;
}
