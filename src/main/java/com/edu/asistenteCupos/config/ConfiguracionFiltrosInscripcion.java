package com.edu.asistenteCupos.config;

import com.edu.asistenteCupos.domain.filtros.FiltrarAnotadosAVariasMaterias;
import com.edu.asistenteCupos.domain.filtros.FiltroAComisionesSinCupo;
import com.edu.asistenteCupos.domain.filtros.FiltroCorrelativas;
import com.edu.asistenteCupos.domain.filtros.FiltroDePeticionInscripcion;
import com.edu.asistenteCupos.domain.filtros.FiltroSuperposicionHoraria;
import com.edu.asistenteCupos.repository.ComisionRepository;
import com.edu.asistenteCupos.repository.HistoriaAcademicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Se usa para configurar los nuevos filtros sobre las peticiones de inscripción
 */
@Configuration
public class ConfiguracionFiltrosInscripcion {
  private final ComisionRepository repositoryComisiones;

  /**
   * @return un filtro de petición de inscripción inicial que conoce su cadena de filtros.
   *
   */

  @Autowired
  public ConfiguracionFiltrosInscripcion(ComisionRepository repositoryComisiones) {
    this.repositoryComisiones = repositoryComisiones;
  }
  @Bean
  public FiltroDePeticionInscripcion cadenaDeFiltros() {
    FiltroDePeticionInscripcion filtro1 = new FiltroCorrelativas(repositoryComisiones);//se comenta porque si no, nadie se inscribe xD
    FiltroDePeticionInscripcion filtro2 = new FiltrarAnotadosAVariasMaterias();
    FiltroDePeticionInscripcion filtro3 = new FiltroAComisionesSinCupo(repositoryComisiones);
    FiltroDePeticionInscripcion filtro4 = new FiltroSuperposicionHoraria();
    filtro3.setFiltroSiguiente(filtro4);
    filtro2.setFiltroSiguiente(filtro3);
   // filtro1.setFiltroSiguiente(filtro2);

    return filtro2;
  }
}