package com.edu.asistenteCupos.service.asignacion.opta.reglas;

import com.edu.asistenteCupos.service.asignacion.opta.model.PeticionAsignableDTO;
import org.optaplanner.core.api.domain.constraintweight.ConstraintConfigurationProvider;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintCollectors;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;
import org.optaplanner.core.api.score.stream.Joiners;

public class AsignacionConstraintProvider implements ConstraintProvider {
  @Override
  public Constraint[] defineConstraints(ConstraintFactory factory) {
//    return new Constraint[]{noSuperarCupo(factory), asignacionSoloAComisionPreferida(
//      factory), unaComisionPorMateria(factory), favorecerPrioridadAlta(
//      factory), favorecerAVZ_COR_SIN(factory), desempatarPorCF(factory)};
    return new Constraint[]{noSuperarCupo(factory)};
  }

  /**
   * 🔒 HARD: Penaliza si una comisión tiene más inscriptos que su cupo.
   */
  @SuppressWarnings("deprecation")
  private Constraint noSuperarCupo(ConstraintFactory factory) {
    return factory.forEach(PeticionAsignableDTO.class).filter(p -> p.getComisionAsignada() != null)
                  .groupBy(PeticionAsignableDTO::getComisionAsignada, ConstraintCollectors.count())
                  .filter((comision, cantidad) -> cantidad > comision.getCupo())
                  .penalizeConfigurable("Cupo excedido",
                    (comision, cantidad) -> cantidad - comision.getCupo());
  }

  /**
   * 🔒 HARD: Penaliza si se asigna una comisión que no estaba entre las preferidas.
   */
//  @SuppressWarnings("deprecation")
//  private Constraint asignacionSoloAComisionPreferida(ConstraintFactory factory) {
//    return factory.forEach(PeticionAsignableDTO.class).filter(
//                    p -> p.getComisionAsignada() != null &&
//                      !p.getCodigosDeComisionPreferidas().contains(p.getComisionAsignada().getCodigo()))
//                  .penalizeConfigurable("Asignación no solicitada", p -> 1);
//  }
//
//  /**
//   * 🔒 HARD: Penaliza si hay más de una comisión asignada a la misma materia.
//   */
//  @SuppressWarnings("deprecation")
//  private Constraint unaComisionPorMateria(ConstraintFactory factory) {
//    return factory.forEachUniquePair(PeticionAsignableDTO.class,
//                    Joiners.equal(PeticionAsignableDTO::getEstudianteId),
//                    Joiners.equal(PeticionAsignableDTO::getMateriaCodigo))
//                  .penalizeConfigurable("Más de una comisión por materia", (a, b) -> 1);
//  }
//
//  /**
//   * 🟡 SOFT: Premia mayor prioridad de la petición.
//   */
//  @SuppressWarnings("deprecation")
//  private Constraint favorecerPrioridadAlta(ConstraintFactory factory) {
//    return factory.forEach(PeticionAsignableDTO.class).filter(p -> p.getComisionAsignada() != null)
//                  .rewardConfigurable("Prioridad alta favorecida",
//                    PeticionAsignableDTO::getPrioridad);
//  }
//
//  /**
//   * 🟡 SOFT: Premia presencia de etiquetas [AVZ], [COR], [SIN].
//   */
//  @SuppressWarnings("deprecation")
//  private Constraint favorecerAVZ_COR_SIN(ConstraintFactory factory) {
//    return factory.forEach(PeticionAsignableDTO.class).filter(p -> p.getComisionAsignada() != null)
//                  .rewardConfigurable("Etiquetas AVZ, COR, SIN favorecidas", p -> {
//                    int score = 0;
//                    if (p.getEtiquetas().contains("[AVZ]"))
//                      score += 10;
//                    if (p.getEtiquetas().contains("[COR]"))
//                      score += 8;
//                    if (p.getEtiquetas().contains("[SIN]"))
//                      score += 6;
//                    return score;
//                  });
//  }

  /**
   * 🟡 SOFT: Premia coeficientes académicos altos.
   */
//  @SuppressWarnings("deprecation")
//  private Constraint desempatarPorCF(ConstraintFactory factory) {
//    return factory.forEach(PeticionAsignableDTO.class).filter(p -> p.getComisionAsignada() != null)
//                  .rewardConfigurable("Coeficiente académico alto favorecido", p -> {
//                    try {
//                      return (int) (Double.parseDouble(p.getHistoria().getCf()) * 10);
//                    } catch (Exception e) {
//                      return 0;
//                    }
//                  });
//  }
}
