package com.edu.asistente_cupos.repository;

import com.edu.asistente_cupos.domain.Materia;

import java.util.List;
import java.util.Optional;

public interface MateriaRepository {
  Materia save(Materia materia);

  List<Materia> findAll();

  Optional<Materia> findByCodigo(String codigoMateria);
}

