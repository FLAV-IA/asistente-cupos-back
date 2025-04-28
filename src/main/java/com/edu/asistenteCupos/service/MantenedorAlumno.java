package com.edu.asistenteCupos.service;

import com.edu.asistenteCupos.domain.Estudiante;
import com.edu.asistenteCupos.domain.PeticionInscripcion;
import com.edu.asistenteCupos.repository.EstudianteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MantenedorAlumno {
    private final EstudianteRepository estudianteRepository;




    public Optional<Estudiante> obtenerAlumno(String legajo) {
        return estudianteRepository.findByCodigo(legajo);
    }
}
