package com.edu.asistente_cupos.repository;

import com.edu.asistente_cupos.domain.HistoriaAcademica;

import java.util.List;

public interface HistoriaAcademicaRepository {
  HistoriaAcademica save(HistoriaAcademica historia);

  List<HistoriaAcademica> findAll();
}
