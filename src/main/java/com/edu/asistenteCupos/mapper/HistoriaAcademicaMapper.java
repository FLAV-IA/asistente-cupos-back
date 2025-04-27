package com.edu.asistenteCupos.mapper;

import com.edu.asistenteCupos.domain.HistoriaAcademica;
import com.edu.asistenteCupos.domain.Materia;
import com.edu.asistenteCupos.service.factory.dto.HistoriaAcademica4Prompt;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {CursadaMapper.class})
public interface HistoriaAcademicaMapper {
  @Mappings({
    @Mapping(target = "totalInscripcionesHistoricas", source = "totalInscripcionesHistoricas"),
    @Mapping(target = "totalHistoricasAprobadas", source = "totalHistoricasAprobadas"),
    @Mapping(target = "coeficiente", source = "coeficiente"),
    @Mapping(target = "cursadasAnteriores", source = "cursadasAnteriores"),
    @Mapping(target = "codigosMateriasInscriptasActuales", source = "inscripcionesActuales", qualifiedByName = "mapInscripcionesActuales")})
  HistoriaAcademica4Prompt toHistoriaAcademica4Prompt(HistoriaAcademica historiaAcademica);

  @Named("mapInscripcionesActuales")
  default List<String> mapInscripcionesActuales(Set<Materia> materias) {
    if (materias == null) {
      return Collections.emptyList();
    }
    return materias.stream().map(Materia::getCodigo).collect(Collectors.toList());
  }
}
