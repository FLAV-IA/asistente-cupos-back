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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EnsambladorDeSugerenciasAceptadasTest {

    private EstudianteRepository estudianteRepository;
    private ComisionRepository comisionRepository;
    private EnsambladorDeSugerenciasAceptadas ensamblador;

    @BeforeEach
    void setUp() {
        estudianteRepository = mock(EstudianteRepository.class);
        comisionRepository = mock(ComisionRepository.class);
        ensamblador = new EnsambladorDeSugerenciasAceptadas(estudianteRepository, comisionRepository);
    }

    @Test
    void ensamblarSugerenciasCreaSugerenciaAceptadaCorrectamente() {
        SugerenciaInscripcionDTO dto = SugerenciaInscripcionDTO.builder()
                .dniEstudiante("123")
                .codigoComision("C1")
                .motivo("alta prioridad")
                .prioridad(1)
                .cupoAsignado(true)
                .build();

        Estudiante estudiante = Estudiante.builder().dni("123").build();
        Materia materia = Materia.builder().nombre("Mat").build();
        Comision comision = Comision.builder().codigo("C1").materia(materia).build();

        when(estudianteRepository.findByDni("123")).thenReturn(Optional.of(estudiante));
        when(comisionRepository.findById("C1")).thenReturn(Optional.of(comision));

        List<SugerenciaInscripcion> resultado = ensamblador.ensamblarSugerencias(List.of(dto));

        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0) instanceof SugerenciaAceptada);
    }

    @Test
    void ensamblarSugerenciasLanzaExcepcionSiDniEsVacio() {
        SugerenciaInscripcionDTO dto = SugerenciaInscripcionDTO.builder()
                .dniEstudiante(" ")
                .codigoComision("C1")
                .build();

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                ensamblador.ensamblarSugerencias(List.of(dto)));

        assertEquals("El DNI del estudiante no puede estar vacio.", ex.getMessage());
    }

    @Test
    void ensamblarSugerenciasLanzaExcepcionSiCodigoComisionEsVacio() {
        SugerenciaInscripcionDTO dto = SugerenciaInscripcionDTO.builder()
                .dniEstudiante("123")
                .codigoComision("")
                .build();

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                ensamblador.ensamblarSugerencias(List.of(dto)));

        assertEquals("El codigo de comision no puede estar vacio.", ex.getMessage());
    }

    @Test
    void ensamblarSugerenciasLanzaExcepcionSiEstudianteNoExiste() {
        SugerenciaInscripcionDTO dto = SugerenciaInscripcionDTO.builder()
                .dniEstudiante("999")
                .codigoComision("C1")
                .build();

        when(estudianteRepository.findByDni("999")).thenReturn(Optional.empty());

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                ensamblador.ensamblarSugerencias(List.of(dto)));

        assertEquals("Estudiante no encontrado: 999", ex.getMessage());
    }

    @Test
    void ensamblarSugerenciasLanzaExcepcionSiComisionNoExiste() {
        SugerenciaInscripcionDTO dto = SugerenciaInscripcionDTO.builder()
                .dniEstudiante("123")
                .codigoComision("C999")
                .build();

        Estudiante estudiante = Estudiante.builder().dni("123").build();
        when(estudianteRepository.findByDni("123")).thenReturn(Optional.of(estudiante));
        when(comisionRepository.findById("C999")).thenReturn(Optional.empty());

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                ensamblador.ensamblarSugerencias(List.of(dto)));

        assertEquals("Comision no encontrada: C999", ex.getMessage());
    }

    @Test
    void ensamblarSugerenciaDesdeAsignacionDevuelveSugerenciaAceptada() {
        Estudiante estudiante = Estudiante.builder().dni("123").build();
        Materia materia = Materia.builder().nombre("Matemática").build();
        Comision comision = Comision.builder().codigo("C1").materia(materia).build();
        Asignacion asignacion = Asignacion.builder()
                .estudiante(estudiante)
                .comision(comision)
                .build();

        SugerenciaInscripcion sugerencia = ensamblador.ensamblarSugerencia(asignacion);

        assertTrue(sugerencia instanceof SugerenciaAceptada);
        assertEquals("Asignado en etapa anterior", sugerencia.motivo());
    }

    @Test
    void ensamblarSugerenciasDesdeAsignacionesMapeaCorrectamente() {
        Estudiante estudiante = Estudiante.builder().dni("123").build();
        Materia materia = Materia.builder().nombre("Matemática").build();
        Comision comision = Comision.builder().codigo("C1").materia(materia).build();
        Asignacion asignacion = Asignacion.builder().estudiante(estudiante).comision(comision).build();

        List<SugerenciaInscripcion> resultado = ensamblador.ensamblarSugerenciasDesdeAsignaciones(List.of(asignacion));

        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0) instanceof SugerenciaAceptada);
    }
}
