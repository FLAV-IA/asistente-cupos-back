package com.edu.asistenteCupos.repository;

import com.edu.asistenteCupos.domain.Materia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MateriaRepository extends JpaRepository<Materia, Long> {
  Optional<Materia> findByCodigo(String codigo);
}
