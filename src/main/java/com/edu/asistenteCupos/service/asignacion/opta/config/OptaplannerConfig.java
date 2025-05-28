package com.edu.asistenteCupos.service.asignacion.opta.config;

import com.edu.asistenteCupos.service.asignacion.opta.model.AsignacionComisionesSolution;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.api.solver.SolverManager;
import org.optaplanner.core.config.solver.SolverConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OptaplannerConfig {

  @Bean
  public SolverConfig solverConfig() {
    return SolverConfig.createFromXmlResource("solverConfig.xml");
  }

  @Bean
  public SolverFactory<AsignacionComisionesSolution> solverFactory(SolverConfig solverConfig) {
    return SolverFactory.create(solverConfig);
  }

  @Bean
  public SolverManager<AsignacionComisionesSolution, Long> solverManager(SolverFactory<AsignacionComisionesSolution> solverFactory) {
    return SolverManager.create(solverFactory);
  }
}
