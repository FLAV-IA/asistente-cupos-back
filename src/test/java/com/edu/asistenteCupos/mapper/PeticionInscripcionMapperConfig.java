package com.edu.asistenteCupos.mapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.edu.asistenteCupos")
public class PeticionInscripcionMapperConfig {
    @Bean
    public ComisionMapper comisionMapper() {
        return new ComisionMapperImpl();
    }

    @Bean
    public PeticionInscripcionMapper peticionInscripcionMapper() {
        return new PeticionInscripcionMapperImpl();
    }
}
