package com.edu.asistenteCupos.domain;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HistoriaAcademicaTest {

    @Test
    void testHaySuperposicionHoraria_CuandoHaySuperposicion() {
        Comision comisionInscripta = Comision.builder()
                .horario("Martes 09:00 a 11:59")
                .build();

        Comision nuevaComision = Comision.builder()
                .horario("Martes 10:00 a 12:00") // se superpone
                .build();

        HistoriaAcademica historia = HistoriaAcademica.builder()
                .inscripcionesActuales(Set.of(comisionInscripta))
                .build();

        try (MockedStatic<ValidadorHorario> mock = mockStatic(ValidadorHorario.class)) {
            mock.when(() -> ValidadorHorario.haySuperposicion(
                            comisionInscripta.getHorario(), nuevaComision.getHorario()))
                    .thenReturn(true);

            assertTrue(historia.haySuperposicionHoraria(nuevaComision));
        }
    }

    @Test
    void testHaySuperposicionHoraria_CuandoNoHaySuperposicion() {
        Comision comisionInscripta = Comision.builder()
                .horario("Lunes 14:00 a 16:00")
                .build();

        Comision nuevaComision = Comision.builder()
                .horario("Martes 10:00 a 12:00")
                .build();

        HistoriaAcademica historia = HistoriaAcademica.builder()
                .inscripcionesActuales(Set.of(comisionInscripta))
                .build();

        try (MockedStatic<ValidadorHorario> mock = mockStatic(ValidadorHorario.class)) {
            mock.when(() -> ValidadorHorario.haySuperposicion(
                            comisionInscripta.getHorario(), nuevaComision.getHorario()))
                    .thenReturn(false);

            assertFalse(historia.haySuperposicionHoraria(nuevaComision));
        }
    }

    @Test
    void testHaySuperposicionHoraria_SinInscripciones() {
        Comision nuevaComision = Comision.builder()
                .horario("Martes 10:00 a 12:00")
                .build();

        HistoriaAcademica historia = HistoriaAcademica.builder()
                .inscripcionesActuales(Set.of()) // sin inscripciones
                .build();

        assertFalse(historia.haySuperposicionHoraria(nuevaComision));
    }
}
