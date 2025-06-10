package com.edu.asistente_cupos.excepcion.config;

import com.edu.asistente_cupos.excepcion.handler.ControllerDummy;
import com.edu.asistente_cupos.excepcion.handler.GlobalExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {
  @Bean
  public ControllerDummy controllerDummy() {
    return new ControllerDummy();
  }

  @Bean
  public GlobalExceptionHandler globalExceptionHandler() {
    return new GlobalExceptionHandler();
  }
}
