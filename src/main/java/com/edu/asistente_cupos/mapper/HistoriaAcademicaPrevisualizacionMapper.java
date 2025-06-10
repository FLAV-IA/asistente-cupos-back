package com.edu.asistente_cupos.mapper;

import com.edu.asistente_cupos.controller.dto.HistoriaAcademicaDTO;
import com.edu.asistente_cupos.domain.HistoriaAcademica;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface HistoriaAcademicaPrevisualizacionMapper {
  @Mapping(source = "estudiante.dni", target = "dni")
  @Mapping(source = "totalInscripcionesHistoricas", target = "totalInscripcionesHistoricas")
  @Mapping(source = "totalHistoricasAprobadas", target = "totalHistoricasAprobadas")
  @Mapping(source = "coeficiente", target = "coeficiente")
  @Mapping(target = "cumpleCorrelativas", constant = "true")
  @Mapping(target = "codigosCursadasAnteriores",
    expression = "java(mapCursadasAnteriores(historia))")
  @Mapping(target = "codigosInscripcionesActuales",
    expression = "java(mapCursadasActuales(historia))")
  HistoriaAcademicaDTO toDto(HistoriaAcademica historia);

  default List<String> mapCursadasAnteriores(HistoriaAcademica historia) {
    return historia.cursadasAnteriores().stream().map(c -> c.getMateria().getCodigo()).toList();
  }

  default Set<String> mapCursadasActuales(HistoriaAcademica historia) {
    return historia.inscripcionesActuales().stream().map(c -> c.getMateria().getCodigo())
                   .collect(java.util.stream.Collectors.toSet());
  }
}