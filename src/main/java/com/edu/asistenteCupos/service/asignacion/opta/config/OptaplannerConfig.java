package com.edu.asistenteCupos.service.asignacion.opta.config;

import com.edu.asistenteCupos.service.asignacion.opta.model.AsignacionComisionesSolution;
import com.edu.asistenteCupos.service.asignacion.opta.model.PeticionAsignableDTO;
import com.edu.asistenteCupos.service.asignacion.opta.reglas.AsignacionConstraintProvider;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.api.solver.SolverManager;
import org.optaplanner.core.config.solver.SolverConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OptaplannerConfig {
  @Bean
  public SolverConfig solverConfig() {
    return new SolverConfig().withSolutionClass(AsignacionComisionesSolution.class)
                             .withEntityClasses(PeticionAsignableDTO.class)
                             .withConstraintProviderClass(AsignacionConstraintProvider.class);
  }

  @Bean
  public SolverManager<AsignacionComisionesSolution, Long> solverManager(SolverFactory<AsignacionComisionesSolution> solverFactory) {
    return SolverManager.create(solverFactory);
  }

  @Bean
  public SolverFactory<AsignacionComisionesSolution> solverFactory(SolverConfig solverConfig) {
    return SolverFactory.create(solverConfig);
  }
}
