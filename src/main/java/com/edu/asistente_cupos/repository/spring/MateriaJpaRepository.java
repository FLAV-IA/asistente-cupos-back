package com.edu.asistente_cupos.repository.spring;

import com.edu.asistente_cupos.domain.Materia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MateriaJpaRepository extends JpaRepository<Materia, String> {
  Optional<Materia> findByCodigo(String codigoMateria);
}
