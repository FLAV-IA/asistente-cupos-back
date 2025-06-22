package com.edu.asistente_cupos.service.asignacion;

import com.edu.asistente_cupos.assembler.EnsambladorDeSugerenciasAceptadas;
import com.edu.asistente_cupos.domain.Asignacion;
import com.edu.asistente_cupos.domain.Comision;
import com.edu.asistente_cupos.domain.Estudiante;
import com.edu.asistente_cupos.domain.Materia;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaAceptada;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaInscripcion;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaRechazada;
import com.edu.asistente_cupos.repository.ComisionRepository;
import com.edu.asistente_cupos.service.AsistenteDeAsignacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AsistenteDeAsignacionTest {

    private AsignadorDeSugerencias asignadorDeSugerencias;
    private ComisionRepository comisionRepository;
    private EnsambladorDeSugerenciasAceptadas ensambladorDeSugerenciasAceptadas;
    private AsistenteDeAsignacion asistenteDeAsignacion;

    @BeforeEach
    void setUp() {
        asignadorDeSugerencias = mock(AsignadorDeSugerencias.class);
        comisionRepository = mock(ComisionRepository.class);
        ensambladorDeSugerenciasAceptadas = mock(EnsambladorDeSugerenciasAceptadas.class);
        asistenteDeAsignacion = new AsistenteDeAsignacion(asignadorDeSugerencias, comisionRepository, ensambladorDeSugerenciasAceptadas);
    }

    @Test
    void alAsignarSugerenciasAceptadaAsignaYRegistrarComisionModificada() {
        // Arrange
        Comision comisionMock = mock(Comision.class);
        when(comisionMock.tieneCupo()).thenReturn(true);
        when(comisionMock.getCodigo()).thenReturn("C1");

        Materia materiaMock = mock(Materia.class);

        Estudiante estudiante = Estudiante.builder()
                .dni("123")
                .legajo("L123")
                .nombre("Juan")
                .mail("juan@mail.com")
                .build();

        SugerenciaAceptada sugerencia = new SugerenciaAceptada(
                estudiante, materiaMock, comisionMock, "Alta prioridad", 90
        );

        when(comisionRepository.findById("C1")).thenReturn(Optional.of(comisionMock));


        asistenteDeAsignacion.asignarSugerencias(List.of(sugerencia));


        verify(asignadorDeSugerencias).asignarSugerencia(sugerencia);
        Set<Comision> modificadas = asistenteDeAsignacion.obtenerComisionesModificadas();
        assertEquals(1, modificadas.size());
        assertTrue(modificadas.contains(comisionMock));
    }

    @Test
    void alAsignarSugerenciasRechazadaAsignaYRegistrarComisionModificada() {
        Comision comisionMock = mock(Comision.class);
        when(comisionMock.tieneCupo()).thenReturn(true);
        when(comisionMock.getCodigo()).thenReturn("C1");
        Materia materiaMock = mock(Materia.class);
        Estudiante estudiante = Estudiante.builder()
                .dni("123")
                .legajo("L123")
                .nombre("Juan")
                .mail("juan@mail.com")
                .build();

        SugerenciaRechazada sugerencia = new SugerenciaRechazada(
                estudiante, materiaMock, comisionMock, "inscripto en varias materias", 20
        );

        when(comisionRepository.findById("C1")).thenReturn(Optional.of(comisionMock));

        asistenteDeAsignacion.asignarSugerencias(List.of(sugerencia));


        verify(asignadorDeSugerencias).asignarSugerencia(sugerencia);
        Set<Comision> modificadas = asistenteDeAsignacion.obtenerComisionesModificadas();
        assertEquals(1, modificadas.size());
        assertTrue(modificadas.contains(comisionMock));
    }

    @Test
    void alAsignarSugerenciasDeUnaComisionQueNoTieneCupoNoHaceNada() {
        Comision comisionMock = mock(Comision.class);
        when(comisionMock.tieneCupo()).thenReturn(false);
        when(comisionMock.getCodigo()).thenReturn("C2");
        Materia materiaMock = mock(Materia.class);
        Estudiante estudiante = Estudiante.builder()
                .dni("456")
                .legajo("L456")
                .nombre("Ana")
                .mail("ana@mail.com")
                .build();
        SugerenciaAceptada sugerencia = new SugerenciaAceptada(estudiante, materiaMock, comisionMock, "Sin prioridad", 2);
        when(comisionRepository.findById("C2")).thenReturn(Optional.of(comisionMock));


        asistenteDeAsignacion.asignarSugerencias(List.of(sugerencia));

        verify(asignadorDeSugerencias, never()).asignarSugerencia(any());
        assertTrue(asistenteDeAsignacion.obtenerComisionesModificadas().isEmpty());
    }

    @Test
    void alAsignarSugerenciasLanzaExcepcionSiComisionNoExiste() {
        Comision comisionDummy = mock(Comision.class);
        when(comisionDummy.getCodigo()).thenReturn("NO_EXISTE");

        Materia materiaMock = mock(Materia.class);

        Estudiante estudiante = Estudiante.builder()
                .dni("789")
                .legajo("L789")
                .nombre("Pedro")
                .mail("pedro@mail.com")
                .build();

        SugerenciaAceptada sugerencia = new SugerenciaAceptada(
                estudiante, materiaMock, comisionDummy, "No existe", 3
        );

        when(comisionRepository.findById("NO_EXISTE")).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                asistenteDeAsignacion.asignarSugerencias(List.of(sugerencia)));
        assertTrue(ex.getMessage().contains("Comision no encontrada"));
    }

    @Test
    void limpiarComisionesModificadasFuncionaCorrectamente() {
        Comision comisionMock = mock(Comision.class);
        when(comisionMock.tieneCupo()).thenReturn(true);
        when(comisionMock.getCodigo()).thenReturn("C3");
        Materia materiaMock = mock(Materia.class);
        Estudiante estudiante = Estudiante.builder()
                .dni("321")
                .legajo("L321")
                .nombre("Laura")
                .mail("laura@mail.com")
                .build();
        SugerenciaAceptada sugerencia = new SugerenciaAceptada(
                estudiante, materiaMock, comisionMock, "Recomendado", 1
        );
        when(comisionRepository.findById("C3")).thenReturn(Optional.of(comisionMock));
        asistenteDeAsignacion.asignarSugerencias(List.of(sugerencia));

        assertFalse(asistenteDeAsignacion.obtenerComisionesModificadas().isEmpty());
        asistenteDeAsignacion.limpiarComisionesModificadas();
        assertTrue(asistenteDeAsignacion.obtenerComisionesModificadas().isEmpty());
    }

    @Test
    void obtenerSugerenciasAsignadas_devuelveCorrectamente() {
        // Arrange
        Asignacion asignacionMock = mock(Asignacion.class);
        SugerenciaInscripcion sugerenciaMock = mock(SugerenciaAceptada.class);

        when(asignadorDeSugerencias.obtenerAsignacionesParciales()).thenReturn(List.of(asignacionMock));
        when(ensambladorDeSugerenciasAceptadas.ensamblarSugerenciasDesdeAsignaciones(List.of(asignacionMock)))
                .thenReturn(List.of(sugerenciaMock));

        List<SugerenciaInscripcion> resultado = asistenteDeAsignacion.obtenerSugerenciasAsignadas();

        assertEquals(1, resultado.size());
        assertEquals(sugerenciaMock, resultado.get(0));
    }
}
