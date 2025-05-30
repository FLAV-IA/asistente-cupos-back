package com.edu.asistente_cupos.mapper;

import com.edu.asistente_cupos.domain.Cursada;
import com.edu.asistente_cupos.domain.prompt.optimizado.Cursada4Prompt;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CursadaMapper {
  @Mapping(source = "materia.codigo", target = "cm")
  @Mapping(source = "fueAprobada", target = "fpm")
  Cursada4Prompt toCursada4Prompt(Cursada cursada);
}
