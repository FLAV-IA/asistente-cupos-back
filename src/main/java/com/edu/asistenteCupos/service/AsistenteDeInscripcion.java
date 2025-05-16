package com.edu.asistenteCupos.service;

import com.edu.asistenteCupos.Utils.llm.BatcherPorTokens;
import com.edu.asistenteCupos.domain.peticion.PeticionInscripcion;
import com.edu.asistenteCupos.domain.peticion.PeticionPriorizada;
import com.edu.asistenteCupos.domain.filtros.FiltroDePeticionInscripcion;
import com.edu.asistenteCupos.domain.prompt.PromptPrinter;
import com.edu.asistenteCupos.domain.sugerencia.SugerenciaInscripcion;
import com.edu.asistenteCupos.service.asignacion.AsignadorDeCupos;
import com.edu.asistenteCupos.service.priorizacion.ConversorResultadoLLM;
import com.edu.asistenteCupos.service.priorizacion.PriorizadorDePeticiones;
import com.edu.asistenteCupos.service.priorizacion.dto.ResultadoPriorizacionLLM;
import com.edu.asistenteCupos.service.prompt.PromptGenerator;
import com.edu.asistenteCupos.service.prompt.PromptTokenizerEstimator;
import com.edu.asistenteCupos.service.traduccion.ConversorSugerenciasLLM;
import com.edu.asistenteCupos.service.traduccion.TraductorDeSugerencias;
import com.edu.asistenteCupos.service.traduccion.dto.SugerenciaInscripcionLLM;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.ToIntFunction;

/**
 * Orquesta todo lo relativo a la inscripción. A.K.A flavia
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AsistenteDeInscripcion {
  private static final int MAX_TOKENS_BATCH = 6000;

  private final FiltroDePeticionInscripcion cadenaDeFiltros;
  private final PriorizadorDePeticiones priorizadorDePeticiones;
  private final TraductorDeSugerencias traductor;
  private final PromptGenerator<List<PeticionInscripcion>> promptGenerator;
  private final ConversorResultadoLLM conversorResultadoLLM;
  private final ConversorSugerenciasLLM conversorSugerenciasLLM;
  private final AsignadorDeCupos asignadorDeCuposManual;
  private final ToIntFunction<PeticionInscripcion> estimadorTokensDePeticion = PromptTokenizerEstimator.estimadorDeObjeto();
  private final ToIntFunction<SugerenciaInscripcion> estimadorPriorizada = PromptTokenizerEstimator.estimadorDeObjeto();

  public List<SugerenciaInscripcion> sugerirInscripcion(List<PeticionInscripcion> peticionesDeInscripcion) {
    List<PeticionInscripcion> filtradas = cadenaDeFiltros.filtrar(peticionesDeInscripcion);

    var batchesEtapa1 = BatcherPorTokens.dividir(filtradas, MAX_TOKENS_BATCH,
      estimadorTokensDePeticion);
    log.info("Etapa priorización - Total de batches: {}", batchesEtapa1.size());

    List<ResultadoPriorizacionLLM> resultadosTotales = batchesEtapa1.stream().peek(
      batch -> log.info("Etapa priorización - Batch con {} peticiones (tokens estimados: {})",
        batch.size(), batch.stream().mapToInt(estimadorTokensDePeticion).sum())).map(
      priorizadorDePeticiones::priorizar).flatMap(List::stream).toList();

    List<PeticionPriorizada> priorizadas = conversorResultadoLLM.desdeResultadosLLM(
      resultadosTotales, filtradas);

    log.info("Etapa asignación - Total de peticiones: {}", priorizadas.size());
    List<SugerenciaInscripcion> sugerenciasAsignadas = asignadorDeCuposManual.asignar(priorizadas);

    var batchesEtapa4 = BatcherPorTokens.dividir(sugerenciasAsignadas, MAX_TOKENS_BATCH,
      estimadorPriorizada);

    log.info("Etapa traducción - Total de batches: {}", batchesEtapa4.size());
    List<SugerenciaInscripcionLLM> sugerenciasLLM = batchesEtapa4.stream().map(traductor::traducir)
                                                                 .flatMap(List::stream).toList();

    return conversorSugerenciasLLM.desdeLLM(sugerenciasLLM);

  }

  public String mostrarPrompt(List<PeticionInscripcion> peticionesDeInscripcion) {
    List<PeticionInscripcion> filtradas = cadenaDeFiltros.filtrar(peticionesDeInscripcion);
    Prompt prompt = promptGenerator.crearPrompt(filtradas);
    System.out.println("el prompt es: \n" + PromptPrinter.imprimir(prompt, true));
    return PromptPrinter.imprimir(prompt, false);
  }
}
