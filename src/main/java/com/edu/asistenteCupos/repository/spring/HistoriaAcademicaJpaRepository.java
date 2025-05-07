package com.edu.asistenteCupos.repository.spring;

import com.edu.asistenteCupos.domain.HistoriaAcademica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HistoriaAcademicaJpaRepository extends JpaRepository<HistoriaAcademica, String> {
  Optional<HistoriaAcademica> findByIdHistoriaAcademica(Long id);
}

