package com.edu.asistente_cupos.repository.spring;


import com.edu.asistente_cupos.domain.Asignacion;
import com.edu.asistente_cupos.domain.Comision;
import com.edu.asistente_cupos.domain.Estudiante;
import com.edu.asistente_cupos.domain.Materia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AsignacionJpaRepository extends JpaRepository<Asignacion, Long> {
    boolean existsByEstudianteAndComision(Estudiante estudiante, Comision comision);

    Optional<Asignacion> findByEstudianteAndComision(Estudiante estudiante, Comision comision);
    @Query("""
    SELECT a FROM Asignacion a
    WHERE a.estudiante = :estudiante AND a.comision.materia = :materia
""")
    Optional<Asignacion> findAsignacionAMateriaDeEstudiante(Estudiante estudiante, Materia materia);
}
