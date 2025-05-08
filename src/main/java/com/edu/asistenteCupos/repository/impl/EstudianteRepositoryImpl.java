package com.edu.asistenteCupos.repository.impl;

import com.edu.asistenteCupos.domain.Estudiante;
import com.edu.asistenteCupos.repository.EstudianteRepository;
import com.edu.asistenteCupos.repository.spring.EstudianteJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class EstudianteRepositoryImpl implements EstudianteRepository {
  private final EstudianteJpaRepository jpaRepository;

  @Override
  public Estudiante save(Estudiante estudiante) {
    return this.jpaRepository.save(estudiante);
  }

  @Override
  public List<Estudiante> findAll() {
    return this.jpaRepository.findAll();
  }

  @Override
  public Optional<Estudiante> findByDni(String dni) {
    return this.jpaRepository.findById(dni);
  }

  @Override
  public List<Estudiante> findByDniIn(Set<String> dni) {
    return jpaRepository.findByDniIn(dni);
  }
}
