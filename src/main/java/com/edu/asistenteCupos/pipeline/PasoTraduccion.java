package com.edu.asistenteCupos.pipeline;

import com.edu.asistenteCupos.Utils.llm.BatcherPorTokens;
import com.edu.asistenteCupos.domain.sugerencia.SugerenciaInscripcion;
import com.edu.asistenteCupos.service.prompt.PromptTokenizerEstimator;
import com.edu.asistenteCupos.service.traduccion.ConversorSugerenciasLLM;
import com.edu.asistenteCupos.service.traduccion.TraductorDeSugerencias;
import com.edu.asistenteCupos.service.traduccion.dto.SugerenciaInscripcionLLM;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.ToIntFunction;

@Component
@RequiredArgsConstructor
@Slf4j
public class PasoTraduccion implements Paso<List<SugerenciaInscripcion>, List<SugerenciaInscripcion>> {

  private static final int MAX_TOKENS_BATCH = 6000;

  private final TraductorDeSugerencias traductor;
  private final ConversorSugerenciasLLM conversor;
  private final ToIntFunction<SugerenciaInscripcion> estimador = PromptTokenizerEstimator.estimadorDeObjeto();

  @Override
  public List<SugerenciaInscripcion> ejecutar(List<SugerenciaInscripcion> input) {
    var batches = BatcherPorTokens.dividir(input, MAX_TOKENS_BATCH, estimador);
    log.info("Etapa traducción - Total de batches: {}", batches.size());

    List<SugerenciaInscripcionLLM> traducidas = batches.stream().map(traductor::traducir)
                                                       .flatMap(List::stream).toList();

    return conversor.desdeLLM(traducidas);
  }
}
