package com.edu.asistente_cupos.service.asignacion;

import com.edu.asistente_cupos.domain.Asignacion;
import com.edu.asistente_cupos.domain.Comision;
import com.edu.asistente_cupos.domain.Estudiante;
import com.edu.asistente_cupos.domain.Materia;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaAceptada;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaInscripcion;
import com.edu.asistente_cupos.repository.AsignacionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AsignadorDeSugerenciasTest {

    private AsignacionRepository asignacionRepository;
    private AsignadorDeSugerencias asignadorDeSugerencias;

    @BeforeEach
    void setUp() {
        asignacionRepository = mock(AsignacionRepository.class);
        asignadorDeSugerencias = new AsignadorDeSugerencias(asignacionRepository);
    }

    @Test
    void asignarSugerenciaYaExisteAsignacionRetornaExistente() {
        Estudiante estudiante = Estudiante.builder().dni("123").build();
        Materia materia = Materia.builder().nombre("Matemática").build();
        Comision comision = Comision.builder().codigo("MAT1").materia(materia).build();
        SugerenciaInscripcion sugerencia = new SugerenciaAceptada(estudiante, materia, comision, "ya asignado", 5);

        Asignacion existente = Asignacion.builder().estudiante(estudiante).comision(comision).build();
        when(asignacionRepository.findByEstudianteAndComision(estudiante, comision))
                .thenReturn(Optional.of(existente));

        Asignacion resultado = asignadorDeSugerencias.asignarSugerencia(sugerencia);

        assertEquals(existente, resultado);
        verify(asignacionRepository, never()).save(any());
    }

    @Test
    void asignarSugerenciaNuevaGuardaYRetorna() {
        Estudiante estudiante = Estudiante.builder().dni("123").build();
        Materia materia = Materia.builder().nombre("Matemática").build();
        Comision comision = Comision.builder().codigo("MAT1").materia(materia).build();
        SugerenciaInscripcion sugerencia = new SugerenciaAceptada(estudiante, materia, comision, "asignación automática", 1);

        when(asignacionRepository.findByEstudianteAndComision(estudiante, comision))
                .thenReturn(Optional.empty());
        when(asignacionRepository.findAsignacionAMateriaDeEstudiante(estudiante, materia)).thenReturn(Optional.empty());

        Asignacion asignacionCreada = Asignacion.builder()
                .estudiante(estudiante)
                .comision(comision)
                .fechaAsignacion(LocalDate.now())
                .build();

        when(asignacionRepository.save(any(Asignacion.class))).thenReturn(asignacionCreada);

        Asignacion resultado = asignadorDeSugerencias.asignarSugerencia(sugerencia);

        assertEquals(asignacionCreada, resultado);
        verify(asignacionRepository).save(any());
    }

    @Test
    void obtenerAsignacionesParcialesDevuelveLista() {
        Asignacion a1 = new Asignacion();
        Asignacion a2 = new Asignacion();
        when(asignacionRepository.findAll()).thenReturn(List.of(a1, a2));

        List<Asignacion> resultado = asignadorDeSugerencias.obtenerAsignacionesParciales();

        assertEquals(2, resultado.size());
        verify(asignacionRepository).findAll();
    }

    @Test
    void eliminarAsignacionExistenteElimina() {
        Comision comision = Comision.builder().codigo("MAT1").build();
        Estudiante estudiante = Estudiante.builder().dni("123").build();
        Asignacion asignacion = Asignacion.builder().estudiante(estudiante).comision(comision).build();

        when(asignacionRepository.findByEstudianteAndComision(estudiante, comision))
                .thenReturn(Optional.of(asignacion));

        asignadorDeSugerencias.eliminarAsignacion(comision, "123");

        verify(asignacionRepository).delete(asignacion);
    }

    @Test
    void eliminarAsignacionInexistenteNoHaceNada() {
        Comision comision = Comision.builder().codigo("MAT1").build();
        Estudiante estudiante = Estudiante.builder().dni("123").build();

        when(asignacionRepository.findByEstudianteAndComision(estudiante, comision))
                .thenReturn(Optional.empty());

        asignadorDeSugerencias.eliminarAsignacion(comision, "123");

        verify(asignacionRepository, never()).delete(any());
    }
}
