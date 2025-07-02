package com.edu.asistente_cupos.filtros;
import com.edu.asistente_cupos.domain.Comision;
import com.edu.asistente_cupos.domain.Estudiante;
import com.edu.asistente_cupos.domain.filtros.FiltroDePeticionYaAsignada;
import com.edu.asistente_cupos.domain.peticion.PeticionInscripcion;
import com.edu.asistente_cupos.domain.peticion.PeticionPorMateria;
import com.edu.asistente_cupos.repository.AsignacionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@DisplayName("FiltroDePeticionYaAsignadaTest")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class FiltroDePeticionYaAsignadaTest {

    private FiltroDePeticionYaAsignada filtro;
    private AsignacionRepository asignacionRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        asignacionRepository = mock(AsignacionRepository.class);
        filtro = new FiltroDePeticionYaAsignada(asignacionRepository);
    }

    private PeticionInscripcion crearPeticion(boolean yaAsignado, boolean incluirComision) {
        PeticionInscripcion peticion = mock(PeticionInscripcion.class);
        Estudiante estudiante = mock(Estudiante.class);
        when(peticion.getEstudiante()).thenReturn(estudiante);

        PeticionPorMateria ppm = mock(PeticionPorMateria.class);
        List<PeticionPorMateria> ppmList = new ArrayList<>();
        ppmList.add(ppm);
        when(peticion.getPeticionPorMaterias()).thenReturn(ppmList);

        List<Comision> comisiones = new ArrayList<>();
        if (incluirComision) {
            Comision comision = mock(Comision.class);
            comisiones.add(comision);
            when(asignacionRepository.existsByEstudianteAndComision(estudiante, comision)).thenReturn(yaAsignado);
        }

        when(ppm.getComisiones()).thenReturn(comisiones);

        return peticion;
    }

    @Test
    @DisplayName("Debe devolver la petición si no está asignada")
    void testFiltrar_peticionNoAsignada_devuelvePeticion() {
        List<PeticionInscripcion> peticiones = List.of(crearPeticion(false, true));
        List<PeticionInscripcion> resultado = filtro.filtrar(peticiones);
        assertEquals(1, resultado.size());
    }

    @Test
    @DisplayName("Debe excluir la petición si ya está asignada")
    void testFiltrar_peticionAsignada_noDevuelvePeticion() {
        List<PeticionInscripcion> peticiones = List.of(crearPeticion(true, true));
        List<PeticionInscripcion> resultado = filtro.filtrar(peticiones);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Debe excluir la petición si no tiene comisiones")
    void testFiltrar_sinComisiones_noDevuelvePeticion() {
        List<PeticionInscripcion> peticiones = List.of(crearPeticion(false, false));
        List<PeticionInscripcion> resultado = filtro.filtrar(peticiones);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Debe retornar lista vacía si no hay peticiones")
    void testFiltrar_listaVacia() {
        List<PeticionInscripcion> resultado = filtro.filtrar(new ArrayList<>());
        assertTrue(resultado.isEmpty());
    }
}