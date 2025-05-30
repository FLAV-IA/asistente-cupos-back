package com.edu.asistente_cupos.repository.impl;

import com.edu.asistente_cupos.domain.Comision;
import com.edu.asistente_cupos.repository.ComisionRepository;
import com.edu.asistente_cupos.repository.spring.ComisionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class ComisionRepositoryImpl implements ComisionRepository {
  private final ComisionJpaRepository jpaRepository;

  @Override
  public Comision save(Comision comision) {
    return jpaRepository.save(comision);
  }

  @Override
  public List<Comision> findAll() {
    return jpaRepository.findAll();
  }

  @Override
  public Optional<Comision> findById(String id) {
    return jpaRepository.findById(id);
  }

  @Override
  public List<Comision> findByCodigoIn(Set<String> codigos) {
    return jpaRepository.findByCodigoIn(codigos);
  }

  @Override
  public int findCupoByCodigo(String codigo) {
    return jpaRepository.findCupoByCodigo(codigo);
  }
}
