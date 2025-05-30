package com.edu.asistente_cupos.repository.impl.memory;

import com.edu.asistente_cupos.domain.Estudiante;
import com.edu.asistente_cupos.repository.EstudianteRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class EstudianteRepositoryInMemory implements EstudianteRepository {
  private final Map<String, Estudiante> data = new HashMap<>();

  @Override
  public Estudiante save(Estudiante estudiante) {
    data.put(estudiante.getDni(), estudiante);
    return estudiante;
  }

  @Override
  public List<Estudiante> findAll() {
    return new ArrayList<>(data.values());
  }

  @Override
  public Optional<Estudiante> findByDni(String dni) {
    return Optional.ofNullable(data.get(dni));
  }

  @Override
  public List<Estudiante> findByDniIn(Set<String> dnis) {
    return dnis.stream().map(data::get).filter(Objects::nonNull).collect(Collectors.toList());
  }
}

