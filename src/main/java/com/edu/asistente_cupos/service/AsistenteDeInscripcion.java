package com.edu.asistente_cupos.service;

import com.edu.asistente_cupos.domain.peticion.PeticionInscripcion;
import com.edu.asistente_cupos.domain.priorizacion.PeticionPorMateriaPriorizada;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaInscripcion;
import com.edu.asistente_cupos.observacion.NombresMetricas;
import com.edu.asistente_cupos.observacion.TimeTracker;
import com.edu.asistente_cupos.pipeline.Paso;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Orquestador principal de inscripción. Ejecuta las etapas en orden secuencial.
 */
@Component
@RequiredArgsConstructor
public class AsistenteDeInscripcion {

  private final Paso<List<PeticionInscripcion>, List<PeticionInscripcion>> filtroPaso;
  private final Paso<List<PeticionInscripcion>, List<PeticionPorMateriaPriorizada>> priorizacionPaso;
  private final Paso<List<PeticionPorMateriaPriorizada>, List<SugerenciaInscripcion>> generacionDeSugerenciasPaso;
  private final Paso<List<SugerenciaInscripcion>, List<SugerenciaInscripcion>> traduccionYConversorPaso;
  private final TimeTracker timeTracker;

  public List<SugerenciaInscripcion> sugerirInscripcion(List<PeticionInscripcion> peticiones) {
    return timeTracker.track(NombresMetricas.ASISTENTE_SUGERENCIA_TOTAL, () -> {
      var filtradas = filtroPaso.ejecutar(peticiones);
      var priorizadas = priorizacionPaso.ejecutar(filtradas);
      var sugerencias = generacionDeSugerenciasPaso.ejecutar(priorizadas);
      var sugerenciasTraducidas = traduccionYConversorPaso.ejecutar(sugerencias);
      return sugerenciasTraducidas;
    });
  }
}

