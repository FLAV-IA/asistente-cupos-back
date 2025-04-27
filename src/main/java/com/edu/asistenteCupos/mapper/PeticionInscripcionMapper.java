package com.edu.asistenteCupos.mapper;

import com.edu.asistenteCupos.controller.dto.PeticionInscripcionCsvDTO;
import com.edu.asistenteCupos.domain.PeticionInscripcion;
import com.edu.asistenteCupos.service.factory.dto.PeticionInscripcion4Prompt;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {HistoriaAcademicaMapper.class, ComisionMapper.class, PeticionInscripcionMappingService.class}, imports = java.util.Collections.class)
public interface PeticionInscripcionMapper {
  default List<PeticionInscripcion> toPeticionInscripcionCsvList(List<PeticionInscripcionCsvDTO> peticiones, @Context PeticionInscripcionMappingService mappingService) {
    return peticiones.stream().map(dto -> toPeticionInscripcion(dto, mappingService))
                     .collect(Collectors.toList());
  }

  @Mappings({
    @Mapping(target = "estudiante", expression = "java(mappingService.buscarEstudiantePorDni(csvDto.getDni()))"),
    @Mapping(target = "materia", expression = "java(mappingService.buscarMateriaPorCodigo(csvDto.getCodigoMateria().trim()))"),
    @Mapping(target = "comisiones", expression = "java(comisionMapper.getComisiones(csvDto.getCodigoMateria(), csvDto.getCodigosComisiones().trim(), mappingService))"),
    @Mapping(target = "cumpleCorrelativa", constant = "false")})
  PeticionInscripcion toPeticionInscripcion(PeticionInscripcionCsvDTO csvDto, @Context PeticionInscripcionMappingService mappingService);

  @Mappings({@Mapping(target = "dni", source = "estudiante.dni"),
    @Mapping(target = "codigoMateria", source = "materia.codigo"),
    @Mapping(target = "codigosComisiones", source = "comisiones"),
    @Mapping(target = "cumpleCorrelativa", source = "cumpleCorrelativa"),
    @Mapping(target = "historiaAcademica", source = "estudiante.historiaAcademica")})
  PeticionInscripcion4Prompt toPeticionInscripcion4Prompt(PeticionInscripcion peticionInscripcion);

  default List<PeticionInscripcion4Prompt> toPeticionInscripcion4PromptList(List<PeticionInscripcion> peticiones) {
    return peticiones.stream().map(this::toPeticionInscripcion4Prompt).collect(Collectors.toList());
  }
}
