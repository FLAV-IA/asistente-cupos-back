package com.edu.asistente_cupos.repository.impl;

import com.edu.asistente_cupos.domain.Materia;
import com.edu.asistente_cupos.repository.MateriaRepository;
import com.edu.asistente_cupos.repository.spring.MateriaJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MateriaRepositoryImpl implements MateriaRepository {
  private final MateriaJpaRepository jpaRepository;

  @Override
  public Materia save(Materia materia) {
    return jpaRepository.save(materia);
  }

  @Override
  public List<Materia> findAll() {
    return jpaRepository.findAll();
  }

  @Override
  public Optional<Materia> findByCodigo(String codigoMateria) {
    return jpaRepository.findByCodigo(codigoMateria);
  }
}
