package com.edu.asistenteCupos.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de seguridad solo para tests.
 * Desactiva CSRF para permitir pruebas de formularios multipart sin token.
 */
@TestConfiguration
public class TestSecurityConfig {

  @Bean
  public SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(auth -> auth.anyRequest().permitAll());

    return http.build();
  }
}
