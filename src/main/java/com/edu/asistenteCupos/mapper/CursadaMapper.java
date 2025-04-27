package com.edu.asistenteCupos.mapper;

import com.edu.asistenteCupos.domain.Cursada;
import com.edu.asistenteCupos.service.factory.dto.Cursada4Prompt;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CursadaMapper {
  @Mapping(source = "materia.codigo", target = "codigoMateria")
  @Mapping(source = "fueAprobada", target = "fueAprobada")
  Cursada4Prompt toCursada4Prompt(Cursada cursada);
}
