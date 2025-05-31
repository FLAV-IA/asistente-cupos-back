package com.edu.asistente_cupos.service.asignacion.opta.reglas;

import com.edu.asistente_cupos.service.asignacion.opta.model.PeticionAsignableDTO;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintCollectors;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;

public class AsignacionConstraintProvider implements ConstraintProvider {
  @Override
  public Constraint[] defineConstraints(ConstraintFactory factory) {
    return new Constraint[]{cupoExcedidoPorComision(factory),       // Regla hard
      peticionNoAsignada(factory),            //️ Soft para permitir inviabilidad controlada
      prioridadAltaFavorecida(factory),       // Elegí al que mejor prioridad tiene
    };
  }

  private Constraint cupoExcedidoPorComision(ConstraintFactory factory) {
    return factory.from(PeticionAsignableDTO.class).filter(p -> p.getComisionAsignada() != null)
                  .groupBy(PeticionAsignableDTO::getComisionAsignada, ConstraintCollectors.count())
                  .filter((comision, count) -> count > comision.getCupo())
                  .penalize("Cupo excedido", HardSoftScore.ofHard(1000));
  }

  private Constraint peticionNoAsignada(ConstraintFactory factory) {
    return factory.from(PeticionAsignableDTO.class).filter(p -> p.getComisionAsignada() == null)
                  .penalize("Petición no asignada", HardSoftScore.ofSoft(1000));
  }

  private Constraint prioridadAltaFavorecida(ConstraintFactory factory) {
    return factory.from(PeticionAsignableDTO.class).filter(p -> p.getComisionAsignada() != null)
                  .reward("Prioridad alta favorecida", HardSoftScore.ONE_SOFT,
                    PeticionAsignableDTO::getPrioridad);
  }

  /**
   * SOFT: Premia coeficientes académicos altos.
   */
  @SuppressWarnings("deprecation")
  private Constraint desempatarPorCF(ConstraintFactory factory) {
    return factory.forEach(PeticionAsignableDTO.class).filter(p -> p.getComisionAsignada() != null)
                  .rewardConfigurable("Coeficiente académico alto favorecido", p -> {
                    try {
                      return (int) (Double.parseDouble(p.getHistoria().getCf()));
                    } catch (Exception e) {
                      return 0;
                    }
                  });
  }
}
