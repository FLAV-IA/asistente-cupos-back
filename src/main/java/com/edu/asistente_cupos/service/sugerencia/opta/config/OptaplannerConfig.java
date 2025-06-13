package com.edu.asistente_cupos.service.sugerencia.opta.config;

import com.edu.asistente_cupos.service.sugerencia.opta.model.SugeridorComisionesSolution;
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
  public SolverFactory<SugeridorComisionesSolution> solverFactory(SolverConfig solverConfig) {
    return SolverFactory.create(solverConfig);
  }

  @Bean
  public SolverManager<SugeridorComisionesSolution, Long> solverManager(SolverFactory<SugeridorComisionesSolution> solverFactory) {
    return SolverManager.create(solverFactory);
  }
}
