package com.edu.asistente_cupos.repository.spring;

import com.edu.asistente_cupos.domain.HistoriaAcademica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HistoriaAcademicaJpaRepository extends JpaRepository<HistoriaAcademica, String> {
  Optional<HistoriaAcademica> findByIdHistoriaAcademica(Long id);
}

