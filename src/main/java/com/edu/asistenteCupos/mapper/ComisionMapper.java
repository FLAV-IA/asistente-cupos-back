package com.edu.asistenteCupos.mapper;

import com.edu.asistenteCupos.domain.Comision;
import org.mapstruct.Mapper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ComisionMapper {
  default List<String> toComisionCodigos(List<Comision> comisiones) {
    return comisiones.stream().map(Comision::getCodigo).collect(Collectors.toList());
  }

  default List<Comision> getComisiones(String codigoMateria, String codigosComisiones, PeticionInscripcionMappingService mappingService) {
    String materia = codigoMateria.trim();
    return Arrays.stream(codigosComisiones.split(",\\s*")).map(
                   codigo -> mappingService.buscarComisionPorCodigo(materia.trim() + "-" + codigo.trim()))
                 .collect(Collectors.toList());
  }
}