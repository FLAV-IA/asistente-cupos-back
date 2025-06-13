package com.edu.asistente_cupos.service.sugerencia.opta.reglas;

import org.optaplanner.core.api.domain.constraintweight.ConstraintConfiguration;
import org.optaplanner.core.api.domain.constraintweight.ConstraintWeight;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

@ConstraintConfiguration
public class ConfiguracionDeRestricciones {

  public static final String CUPO_EXCEDIDO = "Cupo excedido";
  public static final String SUGERENCIA_NO_SOLICITADA = "Asignación no solicitada";
  public static final String MAS_DE_UNA_COMISION_POR_MATERIA = "Más de una comisión por materia";
  public static final String PRIORIDAD_ALTA = "Prioridad alta favorecida";
  public static final String ETIQUETAS_AVZ_COR_SIN = "Etiquetas AVZ, COR, SIN favorecidas";
  public static final String COEFICIENTE_ACADEMICO = "Coeficiente académico alto favorecido";

  @ConstraintWeight(CUPO_EXCEDIDO)
  private final HardSoftScore pesoCupoExcedido = HardSoftScore.ofHard(1000);

  @ConstraintWeight(SUGERENCIA_NO_SOLICITADA)
  private final HardSoftScore pesoSugerenciaNoSolicitada = HardSoftScore.ofHard(100);

  @ConstraintWeight(MAS_DE_UNA_COMISION_POR_MATERIA)
  private final HardSoftScore pesoMasDeUnaComisionPorMateria = HardSoftScore.ofHard(100);

  @ConstraintWeight(PRIORIDAD_ALTA)
  private final HardSoftScore pesoPrioridadAlta = HardSoftScore.ofSoft(10);

  @ConstraintWeight(ETIQUETAS_AVZ_COR_SIN)
  private final HardSoftScore pesoEtiquetas = HardSoftScore.ofSoft(5);

  @ConstraintWeight(COEFICIENTE_ACADEMICO)
  private final HardSoftScore pesoCoeficienteAcademico = HardSoftScore.ofSoft(2);


}
