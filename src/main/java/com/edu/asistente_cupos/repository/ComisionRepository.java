package com.edu.asistente_cupos.repository;

import com.edu.asistente_cupos.domain.Comision;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ComisionRepository {
  Comision save(Comision comision);

  List<Comision> findAll();

  Optional<Comision> findById(String id);

  List<Comision> findByCodigoIn(Set<String> id);

  int findCupoByCodigo(String codigo);
}

