package com.edu.asistenteCupos.mapper;

import com.edu.asistenteCupos.domain.Cursada;
import com.edu.asistenteCupos.domain.prompt.optimizado.Cursada4Prompt;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CursadaMapper {
  @Mapping(source = "materia.codigo", target = "cm")
  @Mapping(source = "fueAprobada", target = "fpm")
  Cursada4Prompt toCursada4Prompt(Cursada cursada);
}
