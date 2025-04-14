package com.edu.asistenteCupos.repository.impl.memory;

import com.edu.asistenteCupos.domain.Materia;
import com.edu.asistenteCupos.repository.MateriaRepository;

import java.util.*;

public class MateriaRepositoryInMemory implements MateriaRepository {
  private final Map<String, Materia> data = new HashMap<>();

  @Override
  public Materia save(Materia materia) {
    data.put(materia.getCodigo(), materia);
    return materia;
  }

  @Override
  public List<Materia> findAll() {
    return new ArrayList<>(data.values());
  }

  @Override
  public Optional<Materia> findByCodigo(String codigoMateria) {
    return Optional.ofNullable(data.get(codigoMateria));
  }
}
