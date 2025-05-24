package com.edu.asistenteCupos.mapper;

import com.edu.asistenteCupos.domain.prompt.optimizado.SugerenciaParaTraducir4Prompt;
import com.edu.asistenteCupos.domain.sugerencia.SugerenciaAsignada;
import com.edu.asistenteCupos.domain.sugerencia.SugerenciaInscripcion;
import com.edu.asistenteCupos.domain.sugerencia.SugerenciaRechazada;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SugerenciaInscripcionPromptMapper {
  public List<SugerenciaParaTraducir4Prompt> toPromptList(List<SugerenciaInscripcion> sugerencias) {
    return sugerencias.stream().map(this::toPromptFormat).toList();
  }

  private SugerenciaParaTraducir4Prompt toPromptFormat(SugerenciaInscripcion sugerencia) {
    if (sugerencia instanceof SugerenciaAsignada asignada) {
      return SugerenciaParaTraducir4Prompt.builder().a(asignada.estudiante().getDni())
                                          .m(asignada.comision().getCodigo()).x(true)
                                          .p(asignada.prioridad()).e(asignada.motivo()).build();
    }

    if (sugerencia instanceof SugerenciaRechazada rechazada) {
      return SugerenciaParaTraducir4Prompt.builder().a(rechazada.estudiante().getDni())
                                          .m(rechazada.comision().getCodigo()).x(false).p(rechazada.prioridad())
                                          .e(rechazada.motivo()).build();
    }

    throw new IllegalArgumentException("Tipo de sugerencia no soportado: " + sugerencia.getClass());
  }
}
