package com.edu.asistenteCupos.mapper;

import com.edu.asistenteCupos.controller.dto.PeticionInscripcionCsvDTO;
import com.edu.asistenteCupos.controller.dto.PeticionInscriptionDTO;
import com.edu.asistenteCupos.domain.PeticionInscripcion;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = PeticionInscripcionMappingService.class, imports = java.util.Collections.class)
public interface PeticionInscripcionMapper {
  default List<PeticionInscripcion> toPeticionInscripcionCsvList(List<PeticionInscripcionCsvDTO> peticiones, @Context PeticionInscripcionMappingService mappingService) {
    return peticiones.stream().map(dto -> toPeticionInscripcion(dto, mappingService))
                     .collect(Collectors.toList());
  }

  @Mappings({@Mapping(target = "estudiante", expression = "java(mappingService.buscarEstudiantePorDni(csvDto.getDni()))"), @Mapping(target = "materia", expression = "java(mappingService.buscarComisionPorCodigo(csvDto.getCodigoComision()).getMateria().getCodigo())"), @Mapping(target = "comisiones", expression = "java(Collections.singletonList(mappingService.buscarComisionPorCodigo(csvDto.getCodigoComision())))"), @Mapping(target = "cumpleCorrelativa", constant = "false")})
  PeticionInscripcion toPeticionInscripcion(PeticionInscripcionCsvDTO csvDto, @Context PeticionInscripcionMappingService mappingService);

  @Named("toJson")
  default String toJson(PeticionInscripcion peticion) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      mapper.enable(SerializationFeature.INDENT_OUTPUT);
      return mapper.writeValueAsString(peticion);
    } catch (Exception e) {
      throw new RuntimeException("Error al convertir PeticionInscripcion a JSON", e);
    }
  }
}
