package com.edu.asistenteCupos.assembler;

import com.edu.asistenteCupos.controller.dto.PeticionInscripcionCsvDTO;
import com.edu.asistenteCupos.domain.Comision;
import com.edu.asistenteCupos.domain.Estudiante;
import com.edu.asistenteCupos.domain.peticion.PeticionInscripcion;
import com.edu.asistenteCupos.domain.peticion.PeticionPorMateria;
import com.edu.asistenteCupos.excepcion.EstudianteNoEncontradoException;
import com.edu.asistenteCupos.repository.ComisionRepository;
import com.edu.asistenteCupos.repository.EstudianteRepository;
import com.edu.asistenteCupos.service.factory.PeticionDeMateriaFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Stream.empty;

@Component
@RequiredArgsConstructor
@Slf4j
public class EnsambladorDePeticiones {
  private final EstudianteRepository estudianteRepository;
  private final ComisionRepository comisionRepository;
  private final PeticionDeMateriaFactory peticionDeMateriaFactory;

  @Transactional(readOnly = true)
  public List<PeticionInscripcion> ensamblarDesdeCsvDto(List<PeticionInscripcionCsvDTO> dtos) {
    Map<String, List<PeticionInscripcionCsvDTO>> peticionesPorDni = dtos.stream().collect(
      Collectors.groupingBy(PeticionInscripcionCsvDTO::getDni));

    Set<String> dniEstudiantes = peticionesPorDni.keySet();
    Map<String, Estudiante> estudiantesPorDni = buscarEstudiantesPorDnis(dniEstudiantes);

    Map<String, Comision> comisionesPorCodigo = buscarComisionesPorCodigos(
      listarCodigosDeComisiones(dtos));

    return ensamblarPeticionesInscripcion(estudiantesPorDni, comisionesPorCodigo, peticionesPorDni);
  }

  private Map<String, Comision> buscarComisionesPorCodigos(Set<String> codigos) {
    return comisionRepository.findByCodigoIn(codigos).stream()
                             .collect(Collectors.toMap(Comision::getCodigo, comision -> comision));
  }

  private Set<String> listarCodigosDeComisiones(List<PeticionInscripcionCsvDTO> dtos) {
    return dtos.stream().flatMap(dto -> {
      String codigos = dto.getCodigosComisiones();
      if (codigos == null || codigos.trim().isEmpty()) {
        log.warn("Se encontró una petición sin códigos de comisión para el DNI: {}", dto.getDni());
        return empty();
      }
      return Arrays.stream(codigos.split(",")).map(String::trim).filter(s -> !s.isEmpty());
    }).collect(Collectors.toSet());
  }

  private List<PeticionInscripcion> ensamblarPeticionesInscripcion(Map<String, Estudiante> estudiantesPorDni, Map<String, Comision> comisionesPorCodigo, Map<String, List<PeticionInscripcionCsvDTO>> peticionesPorDni) {
    return peticionesPorDni.entrySet().stream().map(entry -> {
      String dni = entry.getKey();
      Estudiante estudiante = estudiantesPorDni.get(dni);
      if (estudiante == null) {
        throw new EstudianteNoEncontradoException("No se encontró Estudiante con dni: " + dni);
      }

      List<PeticionPorMateria> peticionesDeMaterias = ensamblarPeticionesDeMateria(
        comisionesPorCodigo, estudiante, entry.getValue());

      return crearPeticionInscripcion(estudiante, peticionesDeMaterias);
    }).collect(Collectors.toList());
  }

  private List<PeticionPorMateria> ensamblarPeticionesDeMateria(Map<String, Comision> comisionesPorCodigo, Estudiante estudiante, List<PeticionInscripcionCsvDTO> dtos) {
    return dtos.stream().map(
                 dto -> peticionDeMateriaFactory.crearPeticionDeMateria(dto, comisionesPorCodigo, estudiante))
               .filter(java.util.Objects::nonNull).collect(Collectors.toList());
  }

  private PeticionInscripcion crearPeticionInscripcion(Estudiante estudiante, List<PeticionPorMateria> peticionesDeMaterias) {
    return PeticionInscripcion.builder().estudiante(estudiante)
                              .peticionPorMaterias(peticionesDeMaterias).build();
  }

  private Map<String, Estudiante> buscarEstudiantesPorDnis(Set<String> dnis) {
    return estudianteRepository.findByDniIn(dnis).stream().collect(
      Collectors.toMap(Estudiante::getDni, estudiante -> estudiante));
  }
}