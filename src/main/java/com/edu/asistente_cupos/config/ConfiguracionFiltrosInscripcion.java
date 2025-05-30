package com.edu.asistente_cupos.config;

import com.edu.asistente_cupos.domain.filtros.FiltrarAnotadosAVariasMaterias;
import com.edu.asistente_cupos.domain.filtros.FiltroAComisionesSinCupo;
import com.edu.asistente_cupos.domain.filtros.FiltroDePeticionInscripcion;
import com.edu.asistente_cupos.domain.filtros.FiltroSuperposicionHoraria;
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
    FiltroDePeticionInscripcion filtro1 = new FiltrarAnotadosAVariasMaterias();
    FiltroDePeticionInscripcion filtro2 = new FiltroAComisionesSinCupo();
    FiltroDePeticionInscripcion filtro3 = new FiltroSuperposicionHoraria();
    filtro2.setFiltroSiguiente(filtro3);
    filtro1.setFiltroSiguiente(filtro2);

    return filtro1;
  }
}