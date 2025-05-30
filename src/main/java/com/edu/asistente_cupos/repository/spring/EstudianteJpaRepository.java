package com.edu.asistente_cupos.repository.spring;

import com.edu.asistente_cupos.domain.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface EstudianteJpaRepository extends JpaRepository<Estudiante, String> {
  List<Estudiante> findByDniIn(Set<String> dni);
}
