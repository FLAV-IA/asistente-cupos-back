package com.edu.asistente_cupos.repository;

import com.edu.asistente_cupos.domain.Asignacion;
import com.edu.asistente_cupos.domain.Comision;
import com.edu.asistente_cupos.domain.Estudiante;
import com.edu.asistente_cupos.domain.Materia;

import java.util.List;
import java.util.Optional;

public interface AsignacionRepository {
    Asignacion save(Asignacion asignacion);

    boolean existsByEstudianteAndComision(Estudiante estudiante, Comision comision);

    List<Asignacion> findAll();

    Optional<Asignacion> findByEstudianteAndComision(Estudiante estudiante, Comision comision);

    Optional<Asignacion> findAsignacionAMateriaDeEstudiante(Estudiante estudiante, Materia materia);

    void delete(Asignacion asignacion);
}
