package com.edu.asistenteCupos.repository.impl.memory;

import com.edu.asistenteCupos.domain.Estudiante;
import com.edu.asistenteCupos.repository.EstudianteRepository;

import java.util.*;

public class EstudianteRepositoryInMemory implements EstudianteRepository {
    private final Map<String, Estudiante> data = new HashMap<>();

    @Override
    public Estudiante save(Estudiante estudiante) {
        data.put(estudiante.getDni(), estudiante);
        return estudiante;
    }

    @Override
    public List<Estudiante> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Optional<Estudiante> findByCodigo(String dni) {
        return Optional.ofNullable(data.get(dni));
    }
}

