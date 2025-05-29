package com.edu.asistente_cupos.mapper;

import com.edu.asistente_cupos.controller.dto.SugerenciaInscripcionDTO;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaAsignada;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaInscripcion;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaRechazada;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SugerenciaInscripcionMapper {
  public SugerenciaInscripcionDTO toDto(SugerenciaInscripcion sugerencia) {
    if (sugerencia instanceof SugerenciaAsignada asignada) {
      return SugerenciaInscripcionDTO.builder()
                                     .nombreEstudiante(asignada.estudiante().getNombre())
                                     .dniEstudiante(asignada.estudiante().getDni())
                                     .nombreMateria(asignada.materia().getNombre())
                                     .codigoComision(asignada.comision().getCodigo())
                                     .motivo(asignada.motivo())
                                     .prioridad(asignada.prioridad()).cupoAsignado(true).build();
    } else if (sugerencia instanceof SugerenciaRechazada rechazada) {
      return SugerenciaInscripcionDTO.builder()
                                     .nombreEstudiante(rechazada.estudiante().getNombre())
                                     .dniEstudiante(rechazada.estudiante().getDni())
                                     .nombreMateria(rechazada.materia().getNombre())
                                     .codigoComision(rechazada.comision().getCodigo()).motivo(rechazada.motivo())
                                     .prioridad(rechazada.prioridad()).cupoAsignado(false)
                                     .build();
    } else {
      throw new IllegalArgumentException(
        "Tipo de sugerencia no soportado: " + sugerencia.getClass().getSimpleName());
    }
  }

  public List<SugerenciaInscripcionDTO> toSugerenciaInscripcionDtoList(List<SugerenciaInscripcion> sugerencias) {
    return sugerencias.stream().map(this::toDto).collect(Collectors.toList());
  }
}
