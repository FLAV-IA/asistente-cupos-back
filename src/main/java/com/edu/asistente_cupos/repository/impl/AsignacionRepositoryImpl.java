package com.edu.asistente_cupos.repository.impl;

import com.edu.asistente_cupos.domain.Asignacion;
import com.edu.asistente_cupos.repository.AsignacionRepository;
import com.edu.asistente_cupos.repository.spring.AsignacionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AsignacionRepositoryImpl implements AsignacionRepository {
    private final AsignacionJpaRepository jpaRepository;

    @Override
    public Asignacion save(Asignacion asignacion) {
        return jpaRepository.save(asignacion);
    }
}
