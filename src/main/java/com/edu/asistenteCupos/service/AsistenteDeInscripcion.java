package com.edu.asistenteCupos.service;

import com.edu.asistenteCupos.domain.peticion.PeticionInscripcion;
import com.edu.asistenteCupos.domain.sugerencia.SugerenciaInscripcion;
import com.edu.asistenteCupos.pipeline.Paso;
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
  private final Paso<List<PeticionInscripcion>, List<com.edu.asistenteCupos.domain.priorizacion.PeticionPorMateriaPriorizada>> priorizacionPaso;
  private final Paso<List<com.edu.asistenteCupos.domain.priorizacion.PeticionPorMateriaPriorizada>, List<SugerenciaInscripcion>> asignacionPaso;
  private final Paso<List<SugerenciaInscripcion>, List<SugerenciaInscripcion>> traduccionYConversorPaso;

  public List<SugerenciaInscripcion> sugerirInscripcion(List<PeticionInscripcion> peticiones) {
    var filtradas = filtroPaso.ejecutar(peticiones);
    var priorizadas = priorizacionPaso.ejecutar(filtradas);
    var sugerencias = asignacionPaso.ejecutar(priorizadas);
    return traduccionYConversorPaso.ejecutar(sugerencias);
  }
}
