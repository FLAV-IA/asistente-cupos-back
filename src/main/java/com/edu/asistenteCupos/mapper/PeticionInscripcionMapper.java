package com.edu.asistenteCupos.mapper;

import com.edu.asistenteCupos.controller.dto.HistoriaAcademicaDTO;
import com.edu.asistenteCupos.controller.dto.PeticionInscripcionCsvDTO;
import com.edu.asistenteCupos.controller.dto.PeticionInscriptionDTO;
import com.edu.asistenteCupos.domain.HistoriaAcademica;
import com.edu.asistenteCupos.domain.PeticionInscripcion;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", imports = java.util.Arrays.class)
public interface PeticionInscripcionMapper {
  List<PeticionInscripcion> toPeticionInscripcionList(List<PeticionInscriptionDTO> peticiones);

  @Mapping(source = "nombre", target = "estudiante.nombre")
  @Mapping(source = "legajo", target = "estudiante.legajo")
  @Mapping(source = "historiaAcademica", target = "estudiante.historiaAcademica")
  @Mapping(source = "correlativa", target = "cumpleCorrelativa")
  PeticionInscripcion toPeticionInscripcion(PeticionInscriptionDTO dto);

  HistoriaAcademica toHistoriaAcademica(HistoriaAcademicaDTO dto);

  @Mappings({@Mapping(source = "nombre", target = "estudiante.nombre"), @Mapping(source = "legajo", target = "estudiante.legajo"), @Mapping(source = ".", target = "estudiante.historiaAcademica", qualifiedByName = "fromCsvToHistoria"), @Mapping(expression = "java(Arrays.asList(csvDto.getComisiones().split(\",\")))", target = "comisiones"), @Mapping(source = "materia", target = "materia"), @Mapping(source = "correlativa", target = "cumpleCorrelativa")})
  PeticionInscripcion toPeticionInscripcion(PeticionInscripcionCsvDTO csvDto);

  @Named("fromCsvToHistoria")
  default HistoriaAcademica toHistoriaAcademica(PeticionInscripcionCsvDTO csvDto) {
    if (csvDto == null) {
      return null;
    }
    HistoriaAcademica historiaAcademica = new HistoriaAcademica();
    historiaAcademica.setInsc3(csvDto.getInsc3());
    historiaAcademica.setInscAct(csvDto.getInscAct());
    historiaAcademica.setAprobUlt(csvDto.getAprobUlt());
    historiaAcademica.setInscTot(csvDto.getInscTot());
    historiaAcademica.setAprobTot(csvDto.getAprobTot());
    historiaAcademica.setRestantes(csvDto.getRestantes());

    return historiaAcademica;
  }


  @Named("toJson")
  default String toJson(PeticionInscripcion peticion) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      mapper.enable(com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT);
      return mapper.writeValueAsString(peticion);
    } catch (Exception e) {
      throw new RuntimeException("Error al convertir PeticionInscripcion a JSON", e);
    }
  }
}