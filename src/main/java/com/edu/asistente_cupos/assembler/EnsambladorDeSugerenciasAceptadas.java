package com.edu.asistente_cupos.assembler;

import com.edu.asistente_cupos.controller.dto.SugerenciaInscripcionDTO;
import com.edu.asistente_cupos.domain.Asignacion;
import com.edu.asistente_cupos.domain.Comision;
import com.edu.asistente_cupos.domain.Estudiante;
import com.edu.asistente_cupos.domain.Materia;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaAceptada;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaInscripcion;
import com.edu.asistente_cupos.repository.ComisionRepository;
import com.edu.asistente_cupos.repository.EstudianteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class EnsambladorDeSugerenciasAceptadas {
    private final EstudianteRepository estudianteRepository;
    private final ComisionRepository comisionRepository;

    public List<SugerenciaAceptada> ensamblarSugerencias(List<SugerenciaInscripcionDTO> dtos) {
        return dtos.stream()
                .map(sugerenciaInscripcionDTO -> {
                    validarSugerencia(sugerenciaInscripcionDTO);
                    Estudiante estudiante = obtenerEstudiante(sugerenciaInscripcionDTO);
                    Comision comision = obtenerComision(sugerenciaInscripcionDTO);
                    Materia materia = comision.getMateria();
                    return crearSugerencia(sugerenciaInscripcionDTO, estudiante, comision, materia);
                }).toList();
    }

    private void validarSugerencia(SugerenciaInscripcionDTO dto) {
        validar(dto.getCodigoComision() == null || dto.getCodigoComision().trim().isEmpty(),
                "El código de comisión no puede estar vacío.");
        validar(dto.getDniEstudiante() == null || dto.getDniEstudiante().trim().isEmpty(),
                "El DNI del estudiante no puede estar vacío.");
        validar(!dto.isCupoAsignado(), "No es una sugerencia asignable.");
    }

    private void validar(boolean condicion, String mensajeError) {
        if (condicion) {
            throw new IllegalArgumentException(mensajeError);
        }
    }

    private SugerenciaAceptada crearSugerencia(SugerenciaInscripcionDTO sugerenciaInscripcionDTO, Estudiante estudiante, Comision comision, Materia materia) {
        return new SugerenciaAceptada(
                estudiante,
                materia,
                comision,
                sugerenciaInscripcionDTO.getMotivo(),
                sugerenciaInscripcionDTO.getPrioridad()
        );
    }

    private Comision obtenerComision(SugerenciaInscripcionDTO sugerenciaInscripcionDTO) {
        return comisionRepository.findById(sugerenciaInscripcionDTO.getCodigoComision())
                .orElseThrow(() -> new IllegalArgumentException("Comision no encontrada: " + sugerenciaInscripcionDTO.getCodigoComision()));
    }

    private Estudiante obtenerEstudiante(SugerenciaInscripcionDTO dto) {
        return estudianteRepository.findByDni(dto.getDniEstudiante())
                .orElseThrow(() -> new IllegalArgumentException("Estudiante no encontrado: " + dto.getDniEstudiante()));
    }

    public SugerenciaInscripcion ensamblarSugerencia(Asignacion asignacion) {
        Estudiante estudiante = asignacion.getEstudiante();
        Comision comision = asignacion.getComision();
        Materia materia = comision.getMateria();

        return new SugerenciaAceptada(
                estudiante,
                materia,
                comision,
                "Asignado en etapa anterior",
                100
        );
    }
    public List<SugerenciaInscripcion> ensamblarSugerenciasDesdeAsignaciones(List<Asignacion> asignaciones) {
        return asignaciones.stream()
                .map(this::ensamblarSugerencia)
                .toList();
    }
}
