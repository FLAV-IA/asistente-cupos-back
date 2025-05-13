package com.edu.asistenteCupos.filtros;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import com.edu.asistenteCupos.domain.Comision;
import com.edu.asistenteCupos.domain.Estudiante;
import com.edu.asistenteCupos.domain.PeticionInscripcion;
import com.edu.asistenteCupos.domain.PeticionPorMateria;
import com.edu.asistenteCupos.domain.filtros.FiltroCorrelativas;
import com.edu.asistenteCupos.repository.ComisionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

class FiltroCorrelativasTest {

    @Mock
    private ComisionRepository comisionRepository;
    @Mock
    private PeticionInscripcion peticionInscripcionMock;
    @Mock
    private PeticionPorMateria peticionPorMateriaMock;
    @Mock
    private Comision comisionMock;
    @Mock
    private Estudiante estudianteMock;

    private FiltroCorrelativas filtroCorrelativas;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        filtroCorrelativas = new FiltroCorrelativas();
    }

    @Test
    void testFiltrar() {
        // Creamos mocks para las peticiones
        List<PeticionInscripcion> peticiones = new ArrayList<>();
        peticiones.add(peticionInscripcionMock);

        // Mock de la estructura PeticionPorMateria
        List<PeticionPorMateria> peticionPorMaterias = new ArrayList<>();
        peticionPorMaterias.add(peticionPorMateriaMock);
        when(peticionInscripcionMock.getPeticionPorMaterias()).thenReturn(peticionPorMaterias);

        // Mock de la estructura Comision
        List<Comision> comisiones = new ArrayList<>();
        comisiones.add(comisionMock);
        when(peticionPorMateriaMock.getComisiones()).thenReturn(comisiones);

        when(comisionRepository.findById(anyString())).thenReturn(Optional.of(comisionMock));
        when (comisionMock.getCodigo()).thenReturn("unaCOmision");

        // Mock para "puedeInscribirse"
        when(estudianteMock.puedeInscribirse(any())).thenReturn(true);
        when(peticionInscripcionMock.getEstudiante()).thenReturn(estudianteMock);

        List<PeticionInscripcion> result = filtroCorrelativas.filtrar(peticiones);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(comisionRepository).findById(anyString());
    }

    @Test
    void testFiltrarSinPeticiones() {
        // Test con lista vacía de peticiones
        List<PeticionInscripcion> peticiones = new ArrayList<>();

        List<PeticionInscripcion> result = filtroCorrelativas.filtrar(peticiones);

        // Verificamos que el resultado sea una lista vacía
        assertTrue(result.isEmpty());
    }
}
