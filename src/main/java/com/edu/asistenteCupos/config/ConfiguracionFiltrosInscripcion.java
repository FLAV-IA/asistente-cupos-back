package com.edu.asistenteCupos.config;

import com.edu.asistenteCupos.domain.filtros.FiltrarAnotadosAVariasMaterias;
import com.edu.asistenteCupos.domain.filtros.FiltroAComisionesSinCupo;
import com.edu.asistenteCupos.domain.filtros.FiltroCorrelativas;
import com.edu.asistenteCupos.domain.filtros.FiltroDePeticionInscripcion;
import com.edu.asistenteCupos.domain.filtros.FiltroSuperposicionHoraria;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Se usa para configurar los nuevos filtros sobre las peticiones de inscripción
 */
@Configuration
public class ConfiguracionFiltrosInscripcion {
  /**
   * @return un filtro de petición de inscripción inicial que conoce su cadena de filtros.
   */
  @Bean
  public FiltroDePeticionInscripcion cadenaDeFiltros() {
    FiltroDePeticionInscripcion filtro1 = new FiltroCorrelativas();
    FiltroDePeticionInscripcion filtro2 = new FiltrarAnotadosAVariasMaterias();
    FiltroDePeticionInscripcion filtro3 = new FiltroAComisionesSinCupo();
    FiltroDePeticionInscripcion filtro4 = new FiltroSuperposicionHoraria();
    filtro3.setFiltroSiguiente(filtro4);
    filtro2.setFiltroSiguiente(filtro3);
    filtro1.setFiltroSiguiente(filtro2);

    return filtro1;
  }
}