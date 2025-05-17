package com.edu.asistenteCupos.service.asignacion.opta.reglas;

import org.optaplanner.core.api.domain.constraintweight.ConstraintConfiguration;
import org.optaplanner.core.api.domain.constraintweight.ConstraintWeight;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

@ConstraintConfiguration
public class ConfiguracionDeRestricciones {

  public static final String CUPOS_EXCEDIDOS = "Cupo excedido";

  @ConstraintWeight(CUPOS_EXCEDIDOS)
  private HardSoftScore pesoCupoExcedido = HardSoftScore.ofHard(1);

  public HardSoftScore getPesoCupoExcedido() {
    return pesoCupoExcedido;
  }

  public void setPesoCupoExcedido(HardSoftScore peso) {
    this.pesoCupoExcedido = peso;
  }
}
