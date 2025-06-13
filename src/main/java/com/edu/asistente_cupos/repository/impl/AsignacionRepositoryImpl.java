package com.edu.asistente_cupos.repository.impl;

import com.edu.asistente_cupos.domain.Asignacion;
import com.edu.asistente_cupos.domain.Comision;
import com.edu.asistente_cupos.domain.Estudiante;
import com.edu.asistente_cupos.repository.AsignacionRepository;
import com.edu.asistente_cupos.repository.spring.AsignacionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AsignacionRepositoryImpl implements AsignacionRepository {
    private final AsignacionJpaRepository jpaRepository;

    @Override
    public Asignacion save(Asignacion asignacion) {
        return jpaRepository.save(asignacion);
    }

    @Override
    public boolean existsByEstudianteAndComision(Estudiante estudiante, Comision comision) {
        return jpaRepository.existsByEstudianteAndComision(estudiante, comision);
    }

    @Override
    public List<Asignacion> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public Optional<Asignacion> findByEstudianteAndComision(Estudiante estudiante, Comision comision) {
        return jpaRepository.findByEstudianteAndComision(estudiante, comision);
    }
}
