package com.edu.asistente_cupos.mapper;

import com.edu.asistente_cupos.domain.Comision;
import com.edu.asistente_cupos.domain.peticion.PeticionInscripcion;
import com.edu.asistente_cupos.domain.prompt.optimizado.PeticionDeMateria4Prompt;
import com.edu.asistente_cupos.domain.prompt.optimizado.PeticionInscripcion4Prompt;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {HistoriaAcademicaMapper.class})
public interface PeticionInscripcionMapper {
  @Mappings({@Mapping(target = "a", source = "estudiante.dni"), @Mapping(target = "h",
    source = "estudiante.historiaAcademica"), @Mapping(target = "p",
    expression = "java(mapPeticiones(peticionInscripcion))")})
  PeticionInscripcion4Prompt toPeticionInscripcion4Prompt(PeticionInscripcion peticionInscripcion);

  List<PeticionInscripcion4Prompt> toPeticionInscripcion4PromptList(List<PeticionInscripcion> peticiones);

  default List<PeticionDeMateria4Prompt> mapPeticiones(PeticionInscripcion peticionInscripcion) {
    if (peticionInscripcion.getPeticionPorMaterias() == null)
      return List.of();

    return peticionInscripcion.getPeticionPorMaterias().stream().map(
                                p -> PeticionDeMateria4Prompt.builder().n(p.getCodigoMateria()).m(
                                                               p.getComisiones().stream().map(Comision::getCodigo).collect(Collectors.toList()))
                                                             .c(p.isCumpleCorrelativa()).build())
                              .collect(Collectors.toList());
  }
}
