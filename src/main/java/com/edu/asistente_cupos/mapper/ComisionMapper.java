package com.edu.asistente_cupos.mapper;

import com.edu.asistente_cupos.controller.dto.InfoComisionDto;
import com.edu.asistente_cupos.domain.Comision;
import com.edu.asistente_cupos.service.sugerencia.opta.model.ComisionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface ComisionMapper {

    @Mappings({
            @Mapping(target = "materiaCodigo", source = "materia.codigo"),
            @Mapping(target = "horario", expression = "java(comision.getHorario() != null ? comision.getHorario().toString() : null)")
    })
    ComisionDTO toDto(Comision comision);

    @Mappings({
            @Mapping(target = "materia", source = "materia.nombre"),
            @Mapping(target = "horario", expression = "java(comision.getHorario() != null ? comision.getHorario().toString() : null)"),
            @Mapping(target = "cantidadInscriptos", expression = "java(comision.estudiantesInscriptos().size())"),
            @Mapping(target = "cuposTotales", source = "cupo")
    })
    InfoComisionDto toComisionActualizadaDto(Comision comision);

    List<ComisionDTO> toDtoList(List<Comision> comisiones);

    Set<ComisionDTO> toDtoList(Set<Comision> comisiones);

    List<InfoComisionDto> toComisionesActualizadasDtoList(List<Comision> comisiones);
}
