package com.edu.asistente_cupos.mapper;

import com.edu.asistente_cupos.domain.cursada.Cursada;
import com.edu.asistente_cupos.domain.prompt.optimizado.Cursada4Prompt;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CursadaMapper {

  @Mapping(source = "materia.codigo", target = "cm")
  @Mapping(target = "fpm", expression = "java(cursada.getEstado().fueAprobada())")
  Cursada4Prompt toCursada4Prompt(Cursada cursada);
}

