package com.edu.asistente_cupos.mapper;

import com.edu.asistente_cupos.controller.dto.PeticionPorMateriaDTO;
import com.edu.asistente_cupos.domain.peticion.PeticionPorMateria;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PeticionPorMateriaMapper {
  @Mapping(target = "nombreMateria", expression = "java(p.getMateria().getNombre())")
  @Mapping(target = "codigoMateria", expression = "java(p.getCodigoMateria())")
  @Mapping(target = "codigosComisionesSolicitadas",
    expression = "java(p.getComisiones().stream().map(c -> c.getCodigo()).toList())")
  @Mapping(source = "cumpleCorrelativa", target = "cumpleCorrelativa")
  PeticionPorMateriaDTO toDto(PeticionPorMateria p);
}
