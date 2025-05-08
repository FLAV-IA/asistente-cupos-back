package com.edu.asistenteCupos.repository.spring;

import com.edu.asistenteCupos.domain.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface EstudianteJpaRepository extends JpaRepository<Estudiante, String> {
  List<Estudiante> findByDniIn(Set<String> dni);
}
