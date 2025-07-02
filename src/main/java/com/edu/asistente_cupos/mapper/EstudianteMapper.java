package com.edu.asistente_cupos.mapper;

import com.edu.asistente_cupos.controller.dto.EstudianteDto;
import com.edu.asistente_cupos.domain.Estudiante;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EstudianteMapper {

    @Mapping(target = "dni", source = "dni")
    @Mapping(target = "nombre", source = "nombre")
    EstudianteDto toDto(Estudiante estudiante);

    List<EstudianteDto> toDtoList(List<Estudiante> estudiantes);


}