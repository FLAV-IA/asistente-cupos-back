package com.edu.asistente_cupos.pipeline;

import com.edu.asistente_cupos.Utils.llm.BatcherPorTokens;
import com.edu.asistente_cupos.domain.peticion.PeticionInscripcion;
import com.edu.asistente_cupos.domain.priorizacion.PeticionPorMateriaPriorizada;
import com.edu.asistente_cupos.observacion.NombresMetricas;
import com.edu.asistente_cupos.observacion.ParalelizadorConMetrica;
import com.edu.asistente_cupos.observacion.TimeTracker;
import com.edu.asistente_cupos.service.priorizacion.ConversorResultadoLLM;
import com.edu.asistente_cupos.service.priorizacion.PriorizadorDePeticiones;
import com.edu.asistente_cupos.service.prompt.PromptTokenizerEstimator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.ToIntFunction;

@Component
@RequiredArgsConstructor
@Slf4j
public class PasoPriorizacion implements Paso<List<PeticionInscripcion>, List<PeticionPorMateriaPriorizada>> {
  private static final int MAX_TOKENS_BATCH = 6000;

  private final PriorizadorDePeticiones priorizador;
  private final ConversorResultadoLLM conversor;
  private final TimeTracker timeTracker;
  private final ParalelizadorConMetrica paralelizador;
  private final ToIntFunction<PeticionInscripcion> estimadorTokens = PromptTokenizerEstimator.estimadorDeObjeto();

  @Override
  public List<PeticionPorMateriaPriorizada> ejecutar(List<PeticionInscripcion> input) {
    return timeTracker.track(NombresMetricas.PRIORIZACION_TOTAL, () -> {
      var batches = BatcherPorTokens.dividir(input, MAX_TOKENS_BATCH, estimadorTokens);
      log.info("Etapa priorización - Total de batches: {}", batches.size());

      var resultados = paralelizador.procesar(NombresMetricas.LLAMADA_BATCH_LLM, batches, batch -> {
        log.info("Etapa priorización - Batch con {} peticiones (tokens estimados: {})",
          batch.size(), batch.stream().mapToInt(estimadorTokens).sum());
        return priorizador.priorizar(batch);
      });

      return conversor.desdeResultadosLLM(resultados.stream().flatMap(List::stream).toList(),
        input);
    });
  }
}