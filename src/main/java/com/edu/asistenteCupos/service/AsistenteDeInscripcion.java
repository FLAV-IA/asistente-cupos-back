package com.edu.asistenteCupos.service;

import com.edu.asistenteCupos.domain.peticion.PeticionInscripcion;
import com.edu.asistenteCupos.domain.sugerencia.SugerenciaInscripcion;
import com.edu.asistenteCupos.pipeline.Paso;
import com.edu.asistenteCupos.mapper.SugerenciaInscripcionMapper;
import com.edu.asistenteCupos.service.asignacion.AsignadorDeCupos;
import com.edu.asistenteCupos.service.priorizacion.ConversorResultadoLLM;
import com.edu.asistenteCupos.service.priorizacion.PriorizadorDePeticiones;
import com.edu.asistenteCupos.service.priorizacion.dto.ResultadoPriorizacionLLM;
import com.edu.asistenteCupos.service.prompt.PromptGenerator;
import com.edu.asistenteCupos.service.prompt.PromptTokenizerEstimator;
import com.edu.asistenteCupos.service.traduccion.ConversorSugerenciasLLM;
import com.edu.asistenteCupos.service.traduccion.TraductorDeSugerencias;
import com.edu.asistenteCupos.service.traduccion.dto.SugerenciaInscripcionLLM;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.openai.OpenAiChatModel;
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
    var priorizadas = priorizacionPaso.ejecutar(peticiones);
    var sugerencias = asignacionPaso.ejecutar(priorizadas);
    return traduccionYConversorPaso.ejecutar(sugerencias);
  }
}
