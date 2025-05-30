package com.edu.asistente_cupos.repository;

import com.edu.asistente_cupos.domain.Estudiante;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EstudianteRepository {
  Estudiante save(Estudiante estudiante);

  List<Estudiante> findAll();

  Optional<Estudiante> findByDni(String dni);

  List<Estudiante> findByDniIn(Set<String> dni);
}
