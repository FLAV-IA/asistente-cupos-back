package com.edu.asistente_cupos.service;

import com.edu.asistente_cupos.domain.peticion.PeticionInscripcion;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaInscripcion;
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
  private final Paso<List<PeticionInscripcion>, List<com.edu.asistente_cupos.domain.priorizacion.PeticionPorMateriaPriorizada>> priorizacionPaso;
  private final Paso<List<com.edu.asistente_cupos.domain.priorizacion.PeticionPorMateriaPriorizada>, List<SugerenciaInscripcion>> asignacionPaso;
  private final Paso<List<SugerenciaInscripcion>, List<SugerenciaInscripcion>> traduccionYConversorPaso;

  public List<SugerenciaInscripcion> sugerirInscripcion(List<PeticionInscripcion> peticiones) {
    var filtradas = filtroPaso.ejecutar(peticiones);
    var priorizadas = priorizacionPaso.ejecutar(filtradas);
    var sugerencias = asignacionPaso.ejecutar(priorizadas);
    return traduccionYConversorPaso.ejecutar(sugerencias);
  }
}
