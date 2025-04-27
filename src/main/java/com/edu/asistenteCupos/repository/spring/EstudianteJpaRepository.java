package com.edu.asistenteCupos.repository.spring;

import com.edu.asistenteCupos.domain.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstudianteJpaRepository extends JpaRepository<Estudiante, String> {
    Optional<Estudiante> findByDNI(String dni);

}
