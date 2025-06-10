package com.edu.asistente_cupos.service.factory;

import com.edu.asistente_cupos.controller.dto.PeticionInscripcionCsvDTO;
import com.edu.asistente_cupos.controller.dto.PeticionInscripcionDTO;
import com.edu.asistente_cupos.domain.Comision;
import com.edu.asistente_cupos.domain.Estudiante;
import com.edu.asistente_cupos.domain.Materia;
import com.edu.asistente_cupos.domain.peticion.PeticionInscripcion;
import com.edu.asistente_cupos.domain.peticion.PeticionPorMateria;
import com.edu.asistente_cupos.repository.MateriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PeticionInscripcionFactory {
  public PeticionInscripcion crearDesdeCsv(PeticionInscripcionCsvDTO dto, Estudiante estudiante, Map<String, Comision> comisionesDisponibles) {
    Set<String> codigos = parsearCodigos(dto.getCodigosComisiones());
    List<Comision> comisiones = filtrarComisiones(codigos, comisionesDisponibles);
    return construirPeticion(estudiante, comisiones);
  }

  public PeticionInscripcion crearDesdeDto(PeticionInscripcionDTO dto, Estudiante estudiante, Map<String, Comision> comisionesDisponibles, MateriaRepository materiaRepository) {
    Set<String> codigos = new HashSet<>(dto.comisionesSolicitadas());
    List<Comision> comisiones = filtrarComisiones(codigos, comisionesDisponibles);
    return construirPeticion(estudiante, comisiones);
  }

  private PeticionInscripcion construirPeticion(Estudiante estudiante, List<Comision> comisiones) {
    Map<Materia, List<Comision>> comisionesPorMateria = comisiones.stream().collect(
      Collectors.groupingBy(Comision::getMateria));

    List<PeticionPorMateria> peticiones = comisionesPorMateria.entrySet().stream().map(entry -> {
      Materia materia = entry.getKey();
      List<Comision> comisionesDeMateria = entry.getValue();
      boolean cumpleCorrelativa = estudiante.puedeInscribirse(materia);
      return PeticionPorMateria.builder().comisiones(comisionesDeMateria)
                               .cumpleCorrelativa(cumpleCorrelativa).build();
    }).toList();

    return PeticionInscripcion.builder().estudiante(estudiante).peticionPorMaterias(peticiones)
                              .build();
  }

  private List<Comision> filtrarComisiones(Set<String> codigos, Map<String, Comision> disponibles) {
    return codigos.stream().map(disponibles::get).filter(Objects::nonNull).toList();
  }

  private Set<String> parsearCodigos(String codigosComisiones) {
    if (codigosComisiones == null || codigosComisiones.isBlank())
      return Set.of();
    return Arrays.stream(codigosComisiones.split(",")).map(String::trim).filter(s -> !s.isEmpty())
                 .collect(Collectors.toSet());
  }
}
