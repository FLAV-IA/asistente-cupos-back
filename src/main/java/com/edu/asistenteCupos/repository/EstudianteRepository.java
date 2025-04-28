package com.edu.asistenteCupos.repository;

import com.edu.asistenteCupos.domain.Estudiante;

import java.util.List;
import java.util.Optional;

public interface EstudianteRepository {
    Estudiante save(Estudiante estudiante);

    List<Estudiante> findAll();

    Optional<Estudiante> findByCodigo(String dni);
}
