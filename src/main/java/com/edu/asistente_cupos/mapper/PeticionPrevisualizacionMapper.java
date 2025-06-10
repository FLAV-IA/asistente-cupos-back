package com.edu.asistente_cupos.mapper;

import com.edu.asistente_cupos.controller.dto.PeticionInscripcionDTO;
import com.edu.asistente_cupos.domain.peticion.PeticionInscripcion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
  uses = {HistoriaAcademicaPrevisualizacionMapper.class, PeticionPorMateriaMapper.class})
public interface PeticionPrevisualizacionMapper {
  @Mapping(source = "estudiante.nombre", target = "nombre")
  @Mapping(source = "estudiante.dni", target = "dni")
  @Mapping(source = "estudiante.historiaAcademica", target = "historiaAcademica")
  @Mapping(source = "peticionPorMaterias", target = "materias")
  PeticionInscripcionDTO toDto(PeticionInscripcion p);
}
