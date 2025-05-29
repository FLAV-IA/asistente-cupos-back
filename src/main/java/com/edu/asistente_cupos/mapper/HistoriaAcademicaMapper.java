package com.edu.asistente_cupos.mapper;

import com.edu.asistente_cupos.domain.Cursada;
import com.edu.asistente_cupos.domain.HistoriaAcademica;
import com.edu.asistente_cupos.domain.Materia;
import com.edu.asistente_cupos.domain.prompt.optimizado.Cursada4Prompt;
import com.edu.asistente_cupos.domain.prompt.optimizado.HistoriaAcademica4Prompt;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Mapper(componentModel = "spring", uses = {CursadaMapper.class})
public abstract class HistoriaAcademicaMapper {
  @Autowired
  protected CursadaMapper cursadaMapper;

  @Mappings({@Mapping(target = "i", source = "totalInscripcionesHistoricas",
    qualifiedByName = "toString"), @Mapping(target = "ap", source = "totalHistoricasAprobadas",
    qualifiedByName = "toString"), @Mapping(target = "cf", source = "coeficiente",
    qualifiedByName = "toString"), @Mapping(target = "ca", source = "cursadasAnteriores",
    qualifiedByName = "mapCursadasNoAprobadas"), @Mapping(target = "ac",
    source = "inscripcionesActuales", qualifiedByName = "mapInscripcionesActuales")})
  public abstract HistoriaAcademica4Prompt toHistoriaAcademica4Prompt(HistoriaAcademica historia);

  @Named("mapInscripcionesActuales")
  public List<String> mapInscripcionesActuales(Set<Materia> Comisiones) {
    if (Comisiones == null)
      return List.of();
    return Comisiones.stream().map(Materia::getCodigo).collect(toList());
  }

  @Named("mapCursadasNoAprobadas")
  public List<String> mapCursadasNoAprobadas(List<Cursada> cursadas) {
    if (cursadas == null)
      return List.of();
    return cursadas.stream().filter(c -> !c.getFueAprobada()).map(cursadaMapper::toCursada4Prompt)
                   .map(Cursada4Prompt::getCm).collect(toList());
  }

  @Named("toString")
  public String toString(Object value) {
    return value != null ? value.toString() : "";
  }
}