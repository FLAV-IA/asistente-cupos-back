package com.edu.asistenteCupos.repository;

import com.edu.asistenteCupos.domain.Estudiante;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EstudianteRepository {
  Estudiante save(Estudiante estudiante);

  List<Estudiante> findAll();

  Optional<Estudiante> findByDni(String dni);

  List<Estudiante> findByDniIn(Set<String> dni);
}
