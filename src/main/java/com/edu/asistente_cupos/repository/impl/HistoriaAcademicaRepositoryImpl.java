package com.edu.asistente_cupos.repository.impl;

import com.edu.asistente_cupos.domain.HistoriaAcademica;
import com.edu.asistente_cupos.repository.HistoriaAcademicaRepository;
import com.edu.asistente_cupos.repository.spring.HistoriaAcademicaJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class HistoriaAcademicaRepositoryImpl implements HistoriaAcademicaRepository {
  private final HistoriaAcademicaJpaRepository jpaRepository;

  @Override
  public HistoriaAcademica save(HistoriaAcademica historia) {
    return jpaRepository.save(historia);
  }

  @Override
  public List<HistoriaAcademica> findAll() {
    return jpaRepository.findAll();
  }
}
