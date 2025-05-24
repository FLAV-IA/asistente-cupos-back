package com.edu.asistenteCupos.pipeline;

import com.edu.asistenteCupos.Utils.llm.BatcherPorTokens;
import com.edu.asistenteCupos.domain.peticion.PeticionInscripcion;
import com.edu.asistenteCupos.domain.priorizacion.PeticionPorMateriaPriorizada;
import com.edu.asistenteCupos.service.priorizacion.ConversorResultadoLLM;
import com.edu.asistenteCupos.service.priorizacion.PriorizadorDePeticiones;
import com.edu.asistenteCupos.service.prompt.PromptTokenizerEstimator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.ToIntFunction;

@Component
@RequiredArgsConstructor
@Slf4j
public class PasoPriorizacion implements Paso<List<PeticionInscripcion>, List<PeticionPorMateriaPriorizada>> {
  private static final int MAX_TOKENS_BATCH = 9000;

  private final PriorizadorDePeticiones priorizador;
  private final ConversorResultadoLLM conversor;
  private final ToIntFunction<PeticionInscripcion> estimadorTokens = PromptTokenizerEstimator.estimadorDeObjeto();

  @Override
  public List<PeticionPorMateriaPriorizada> ejecutar(List<PeticionInscripcion> input) {
    var batches = BatcherPorTokens.dividir(input, MAX_TOKENS_BATCH, estimadorTokens);
    log.info("Etapa priorización - Total de batches: {}", batches.size());

    var resultadosLLM = batches.stream().peek(
                                 batch -> log.info("Etapa priorización - Batch con {} peticiones (tokens estimados: {})",
                                   batch.size(), batch.stream().mapToInt(estimadorTokens).sum())).map(priorizador::priorizar)
                               .flatMap(List::stream).toList();

    return conversor.desdeResultadosLLM(resultadosLLM, input);
  }
}
