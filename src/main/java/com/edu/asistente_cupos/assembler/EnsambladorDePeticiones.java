package com.edu.asistente_cupos.assembler;

import com.edu.asistente_cupos.Utils.TriFunction;
import com.edu.asistente_cupos.controller.dto.PeticionInscripcionCsvDTO;
import com.edu.asistente_cupos.controller.dto.PeticionInscripcionDTO;
import com.edu.asistente_cupos.domain.Comision;
import com.edu.asistente_cupos.domain.Estudiante;
import com.edu.asistente_cupos.domain.peticion.PeticionInscripcion;
import com.edu.asistente_cupos.excepcion.EstudianteNoEncontradoException;
import com.edu.asistente_cupos.repository.ComisionRepository;
import com.edu.asistente_cupos.repository.EstudianteRepository;
import com.edu.asistente_cupos.repository.MateriaRepository;
import com.edu.asistente_cupos.service.factory.PeticionInscripcionFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
@Slf4j
public class EnsambladorDePeticiones {
  private final EstudianteRepository estudianteRepository;
  private final ComisionRepository comisionRepository;
  private final MateriaRepository materiaRepository;
  private final PeticionInscripcionFactory peticionInscripcionFactory;

  @Transactional(readOnly = true)
  public List<PeticionInscripcion> ensamblarDesdeCsvDto(List<PeticionInscripcionCsvDTO> dtos) {
    return ensamblarGenerico(dtos, PeticionInscripcionCsvDTO::getDni,
      this::listarCodigosDeComisionesCsv, peticionInscripcionFactory::crearDesdeCsv);
  }

  @Transactional(readOnly = true)
  public List<PeticionInscripcion> ensamblarDesdeDto(List<PeticionInscripcionDTO> dtos) {
    return ensamblarGenerico(dtos, PeticionInscripcionDTO::dni, this::listarCodigosDeComisionesDto,
      (dto, estudiante, comisiones) -> peticionInscripcionFactory.crearDesdeDto(dto, estudiante,
        comisiones, materiaRepository));
  }

  private <T> List<PeticionInscripcion> ensamblarGenerico(List<T> dtos, Function<T, String> dniExtractor, Function<List<T>, Set<String>> codigosComisionesExtractor, TriFunction<T, Estudiante, Map<String, Comision>, PeticionInscripcion> factory) {
    Set<String> dnis = dtos.stream().map(dniExtractor).collect(Collectors.toSet());
    Map<String, Estudiante> estudiantesPorDni = buscarEstudiantesPorDnis(dnis);

    Set<String> codigos = codigosComisionesExtractor.apply(dtos);
    Map<String, Comision> comisionesPorCodigo = buscarComisionesPorCodigos(codigos);

    return dtos.stream().map(dto -> {
      String dni = dniExtractor.apply(dto);
      Estudiante estudiante = estudiantesPorDni.get(dni);
      if (estudiante == null) {
        throw new EstudianteNoEncontradoException("No se encontró estudiante con DNI: " + dni);
      }
      return factory.apply(dto, estudiante, comisionesPorCodigo);
    }).filter(Objects::nonNull).toList();
  }

  private Map<String, Estudiante> buscarEstudiantesPorDnis(Set<String> dnis) {
    return estudianteRepository.findByDniIn(dnis).stream()
                               .collect(Collectors.toMap(Estudiante::getDni, e -> e));
  }

  private Map<String, Comision> buscarComisionesPorCodigos(Set<String> codigos) {
    return comisionRepository.findByCodigoIn(codigos).stream()
                             .collect(Collectors.toMap(Comision::getCodigo, c -> c));
  }

  private Set<String> listarCodigosDeComisionesCsv(List<PeticionInscripcionCsvDTO> dtos) {
    return dtos.stream().flatMap(dto -> {
      String codigos = dto.getCodigosComisiones();
      if (codigos == null || codigos.trim().isEmpty()) {
        log.warn("Petición sin códigos de comisión para el DNI: {}", dto.getDni());
        return Stream.empty();
      }
      return Arrays.stream(codigos.split(",")).map(String::trim).filter(s -> !s.isEmpty());
    }).collect(Collectors.toSet());
  }

  private Set<String> listarCodigosDeComisionesDto(List<PeticionInscripcionDTO> dtos) {
    return dtos.stream().flatMap(dto -> dto.comisionesSolicitadas().stream())
               .collect(Collectors.toSet());
  }
}