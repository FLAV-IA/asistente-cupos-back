package com.edu.asistenteCupos.repository;

import com.edu.asistenteCupos.domain.HistoriaAcademica;
import com.edu.asistenteCupos.domain.Materia;

import java.util.List;

public interface HistoriaAcademicaRepository {
  HistoriaAcademica save(HistoriaAcademica historia);

  List<HistoriaAcademica> findAll();
}
