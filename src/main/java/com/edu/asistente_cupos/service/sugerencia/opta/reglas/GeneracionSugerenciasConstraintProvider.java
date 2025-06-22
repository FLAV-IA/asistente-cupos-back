package com.edu.asistente_cupos.service.sugerencia.opta.reglas;

import com.edu.asistente_cupos.service.sugerencia.opta.model.PeticionAceptableDTO;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintCollectors;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;

public class GeneracionSugerenciasConstraintProvider implements ConstraintProvider {
  @Override
  public Constraint[] defineConstraints(ConstraintFactory factory) {
    return new Constraint[]{cupoExcedidoPorComision(factory),       // Regla hard
      peticionNoAsignada(factory),            //️ Soft para permitir inviabilidad controlada
      prioridadAltaFavorecida(factory),       // Elegí al que mejor prioridad tiene
    };
  }

  private Constraint cupoExcedidoPorComision(ConstraintFactory factory) {
    return factory.from(PeticionAceptableDTO.class).filter(p -> p.getComisionSugerida() != null)
                  .groupBy(PeticionAceptableDTO::getComisionSugerida, ConstraintCollectors.count())
                  .filter((comision, count) -> count > comision.getCupo())
                  .penalize("Cupo excedido", HardSoftScore.ofHard(1000));
  }

  private Constraint peticionNoAsignada(ConstraintFactory factory) {
    return factory.from(PeticionAceptableDTO.class).filter(p -> p.getComisionSugerida() == null)
                  .penalize("Petición no asignada", HardSoftScore.ofSoft(1000));
  }

  private Constraint prioridadAltaFavorecida(ConstraintFactory factory) {
    return factory.from(PeticionAceptableDTO.class).filter(p -> p.getComisionSugerida() != null)
                  .reward("Prioridad alta favorecida", HardSoftScore.ONE_SOFT,
                    PeticionAceptableDTO::getPrioridad);
  }

  /**
   * SOFT: Premia coeficientes académicos altos.
   */
  @SuppressWarnings("deprecation")
  private Constraint desempatarPorCF(ConstraintFactory factory) {
    return factory.forEach(PeticionAceptableDTO.class).filter(p -> p.getComisionSugerida() != null)
                  .rewardConfigurable("Coeficiente académico alto favorecido", p -> {
                    try {
                      return (int) (Double.parseDouble(p.getHistoria().getCf()));
                    } catch (Exception e) {
                      return 0;
                    }
                  });
  }
}
