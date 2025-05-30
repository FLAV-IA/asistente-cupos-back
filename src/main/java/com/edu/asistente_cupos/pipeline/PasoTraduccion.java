package com.edu.asistente_cupos.pipeline;

import com.edu.asistente_cupos.Utils.llm.BatcherPorTokens;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaInscripcion;
import com.edu.asistente_cupos.observacion.NombresMetricas;
import com.edu.asistente_cupos.observacion.ParalelizadorConMetrica;
import com.edu.asistente_cupos.observacion.TimeTracker;
import com.edu.asistente_cupos.service.prompt.PromptTokenizerEstimator;
import com.edu.asistente_cupos.service.traduccion.ConversorSugerenciasLLM;
import com.edu.asistente_cupos.service.traduccion.TraductorDeSugerencias;
import com.edu.asistente_cupos.service.traduccion.dto.SugerenciaInscripcionLLM;
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
  private final TimeTracker timeTracker;
  private final ParalelizadorConMetrica paralelizador;
  private final ToIntFunction<SugerenciaInscripcion> estimador = PromptTokenizerEstimator.estimadorDeObjeto();

  @Override
  public List<SugerenciaInscripcion> ejecutar(List<SugerenciaInscripcion> input) {
    return timeTracker.track(NombresMetricas.TRADUCCION_TOTAL, () -> {
      var batches = BatcherPorTokens.dividir(input, MAX_TOKENS_BATCH, estimador);
      log.info("Etapa traducción - Total de batches: {}", batches.size());

      List<SugerenciaInscripcionLLM> traducidas = paralelizador
        .procesar(NombresMetricas.TRADUCCION_BATCH, batches, batch -> {
          log.info("Etapa traducción - Traduciendo batch con {} sugerencias (tokens estimados: {})",
            batch.size(), batch.stream().mapToInt(estimador).sum());
          return traductor.traducir(batch);
        }).stream().flatMap(List::stream).toList();

      return conversor.desdeLLM(traducidas);
    });
  }
}
