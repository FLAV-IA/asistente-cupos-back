package com.edu.asistenteCupos.filtros;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.edu.asistenteCupos.domain.Comision;
import com.edu.asistenteCupos.domain.Estudiante;
import com.edu.asistenteCupos.domain.PeticionInscripcion;
import com.edu.asistenteCupos.domain.PeticionPorMateria;
import com.edu.asistenteCupos.domain.filtros.FiltroAComisionesSinCupo;
import com.edu.asistenteCupos.domain.filtros.FiltroDePeticionInscripcion;
import com.edu.asistenteCupos.repository.ComisionRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

class FiltroAComisionesSinCupoTest {

    @Mock
    private ComisionRepository comisionRepository;

    private FiltroAComisionesSinCupo filtro;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        filtro = new FiltroAComisionesSinCupo(comisionRepository);
    }

    @Test
    void filtrarVariasPeticionesAlgunasConCupo() {
        // Estudiante 1
        Estudiante estudiante1 = Estudiante.builder().dni("45072112").nombre("Juan").build();
        PeticionInscripcion peticion1 = PeticionInscripcion.builder()
                .estudiante(estudiante1)
                .build();
        PeticionPorMateria ppm1 = PeticionPorMateria.builder().build();
        Comision c1a = Comision.builder().codigo("90028-C-1-G14").build();
        Comision c1b = Comision.builder().codigo("90028-C-2-G14").build();
        ppm1.setComisiones(List.of(c1a, c1b));
        peticion1.setPeticionPorMaterias(List.of(ppm1));

        // Estudiante 2
        Estudiante estudiante2 = Estudiante.builder().dni("44170649").nombre("Ana").build();
        PeticionInscripcion peticion2 = PeticionInscripcion.builder()
                .estudiante(estudiante2)
                .build();
        PeticionPorMateria ppm2 = PeticionPorMateria.builder().build();
        Comision c2a = Comision.builder().codigo("1060-1-CP").build();
        ppm2.setComisiones(List.of(c2a));
        peticion2.setPeticionPorMaterias(List.of(ppm2));

        // Comisiones con y sin cupo
        when(comisionRepository.findCupoByCodigo( "90028-C-1-G14")).thenReturn(10); // con cupo
        when(comisionRepository.findCupoByCodigo( "90028-C-2-G14")).thenReturn(0);  // sin cupo
        when(comisionRepository.findCupoByCodigo( "1060-1-CP")).thenReturn(0);      // sin cupo

        List<PeticionInscripcion> resultado = filtro.filtrar(List.of(peticion1, peticion2));

        // Solo peticion1 debe quedar, con una sola comision
        assertEquals(1, resultado.size());
        PeticionInscripcion resultado1 = resultado.get(0);
        assertEquals("45072112", resultado1.getEstudiante().getDni());
        List<Comision> comisionesFiltradas = resultado1.getPeticionPorMaterias().get(0).getComisiones();
        assertEquals(1, comisionesFiltradas.size());
        assertEquals("90028-C-1-G14", comisionesFiltradas.get(0).getCodigo());
    }

    @Test
    void filtrarTodosSinCupo() {
        Estudiante estudiante1 = Estudiante.builder().dni("40955499").nombre("Lucas").build();
        PeticionInscripcion pet1 = PeticionInscripcion.builder()
                .estudiante(estudiante1)
                .build();
        PeticionPorMateria ppm1 = PeticionPorMateria.builder().build();
        ppm1.setComisiones(List.of(Comision.builder().codigo("1043-1-G14").build()));
        pet1.setPeticionPorMaterias(List.of(ppm1));

        Estudiante estudiante2 = Estudiante.builder().dni("40946592").nombre("Mica").build();
        PeticionInscripcion pet2 = PeticionInscripcion.builder()
                .estudiante(estudiante2)
                .build();
        PeticionPorMateria ppm2 = PeticionPorMateria.builder().build();
        ppm2.setComisiones(List.of(Comision.builder().codigo("1032-4-G14").build()));
        pet2.setPeticionPorMaterias(List.of(ppm2));

        when(comisionRepository.findCupoByCodigo( anyString())).thenReturn(0);

        List<PeticionInscripcion> resultado = filtro.filtrar(List.of(pet1, pet2));

        assertTrue(resultado.isEmpty(), "Todas las comisiones estan sin cupo, debe quedar vacio");
    }

    @Test
    void filtrarConCupoYFiltroSiguiente() {
        Estudiante estudiante = Estudiante.builder().dni("45297833").nombre("Carla").build();
        PeticionInscripcion peticion = PeticionInscripcion.builder()
                .estudiante(estudiante)
                .build();
        PeticionPorMateria ppm = PeticionPorMateria.builder().build();
        Comision comision = Comision.builder().codigo("1032-8-G14").build();
        ppm.setComisiones(List.of(comision));
        peticion.setPeticionPorMaterias(List.of(ppm));

        when(comisionRepository.findCupoByCodigo( "1032-8-G14")).thenReturn(5);

        // siguiente filtro mockeado
        FiltroDePeticionInscripcion siguiente = mock(FiltroDePeticionInscripcion.class);
        filtro.setFiltroSiguiente(siguiente);
        when(siguiente.filtrar(any())).thenReturn(List.of(peticion));

        List<PeticionInscripcion> resultado = filtro.filtrar(List.of(peticion));

        verify(siguiente, times(1)).filtrar(any());
        assertEquals(1, resultado.size());
        assertEquals("45297833", resultado.get(0).getEstudiante().getDni());
    }
    @Test
    void omitirPeticionInscripcionSiNingunaComisionTieneCupo() {
        Estudiante estudiante = Estudiante.builder().dni("40000000").nombre("Pedro").build();
        PeticionPorMateria ppm1 = PeticionPorMateria.builder().build();
        ppm1.setComisiones(List.of(
                Comision.builder().codigo("A").build(),
                Comision.builder().codigo("B").build()
        ));

        PeticionInscripcion peticion = PeticionInscripcion.builder()
                .estudiante(estudiante)
                .peticionPorMaterias(List.of(ppm1))
                .build();

        when(comisionRepository.findCupoByCodigo( "A")).thenReturn(0);
        when(comisionRepository.findCupoByCodigo( "B")).thenReturn(0);

        List<PeticionInscripcion> resultado = filtro.filtrar(List.of(peticion));

        assertTrue(resultado.isEmpty(), "La peticion debe ser omitida si todas sus comisiones estan sin cupo");
    }
    @Test
    void omitirPeticionPorMateriaSiTodasSusComisionesEstanSinCupo() {
        Estudiante estudiante = Estudiante.builder().dni("40000001").nombre("Maria").build();

        PeticionPorMateria sinCupo = PeticionPorMateria.builder().build();
        sinCupo.setComisiones(List.of(Comision.builder().codigo("C1").build()));

        PeticionPorMateria conCupo = PeticionPorMateria.builder().build();
        conCupo.setComisiones(List.of(Comision.builder().codigo("C2").build()));

        PeticionInscripcion peticion = PeticionInscripcion.builder()
                .estudiante(estudiante)
                .peticionPorMaterias(List.of(sinCupo, conCupo))
                .build();

        when(comisionRepository.findCupoByCodigo( "C1")).thenReturn(0);  // sin cupo
        when(comisionRepository.findCupoByCodigo( "C2")).thenReturn(5);  // con cupo

        List<PeticionInscripcion> resultado = filtro.filtrar(List.of(peticion));

        assertEquals(1, resultado.size());
        PeticionInscripcion resultadoFinal = resultado.get(0);
        assertEquals(1, resultadoFinal.getPeticionPorMaterias().size());
        assertEquals("C2", resultadoFinal.getPeticionPorMaterias().get(0).getComisiones().get(0).getCodigo());
    }

    @Test
    void omitirSoloComisionesSinCupoDeUnaPeticionPorMateria() {
        Estudiante estudiante = Estudiante.builder().dni("40000002").nombre("Julian").build();

        Comision sinCupo = Comision.builder().codigo("D1").build();
        Comision conCupo = Comision.builder().codigo("D2").build();

        PeticionPorMateria ppm = PeticionPorMateria.builder().build();
        ppm.setComisiones(List.of(sinCupo, conCupo));

        PeticionInscripcion peticion = PeticionInscripcion.builder()
                .estudiante(estudiante)
                .peticionPorMaterias(List.of(ppm))
                .build();

        when(comisionRepository.findCupoByCodigo( "D1")).thenReturn(0);   // sin cupo
        when(comisionRepository.findCupoByCodigo( "D2")).thenReturn(7);   // con cupo

        List<PeticionInscripcion> resultado = filtro.filtrar(List.of(peticion));

        assertEquals(1, resultado.size());
        PeticionInscripcion resultadoFinal = resultado.get(0);
        List<Comision> comisionesRestantes = resultadoFinal.getPeticionPorMaterias().get(0).getComisiones();
        assertEquals(1, comisionesRestantes.size());
        assertEquals("D2", comisionesRestantes.get(0).getCodigo());
    }

    @Test
    void peticionPorMateriaConTodasComisionesSinCupoEsEliminada() {
        Estudiante estudiante = Estudiante.builder().dni("40000003").nombre("Sofia").build();

        PeticionPorMateria ppm1 = PeticionPorMateria.builder().build();
        ppm1.setComisiones(List.of(Comision.builder().codigo("E1").build(), Comision.builder().codigo("E2").build()));

        PeticionInscripcion peticion = PeticionInscripcion.builder()
                .estudiante(estudiante)
                .peticionPorMaterias(List.of(ppm1))
                .build();

        when(comisionRepository.findCupoByCodigo( "E1")).thenReturn(0);
        when(comisionRepository.findCupoByCodigo( "E2")).thenReturn(0);

        List<PeticionInscripcion> resultado = filtro.filtrar(List.of(peticion));

        assertTrue(resultado.isEmpty(), "Debe eliminarse si la unica peticion de materia no tiene comisiones con cupo");
    }

}
