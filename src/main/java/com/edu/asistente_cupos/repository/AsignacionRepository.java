package com.edu.asistente_cupos.repository;

import com.edu.asistente_cupos.domain.Asignacion;
import com.edu.asistente_cupos.domain.Comision;
import com.edu.asistente_cupos.domain.Estudiante;

import java.util.List;

public interface AsignacionRepository {
    Asignacion save(Asignacion asignacion);

    boolean existsByEstudianteAndComision(Estudiante estudiante, Comision comision);

    List<Asignacion> findAll();
}
