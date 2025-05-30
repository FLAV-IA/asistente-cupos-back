package com.edu.asistente_cupos.filtros;

import com.edu.asistente_cupos.domain.Comision;
import com.edu.asistente_cupos.domain.Estudiante;
import com.edu.asistente_cupos.domain.Materia;
import com.edu.asistente_cupos.domain.filtros.FiltroCorrelativas;
import com.edu.asistente_cupos.domain.peticion.PeticionInscripcion;
import com.edu.asistente_cupos.domain.peticion.PeticionPorMateria;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("FiltroCorrelativasTest")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class FiltroCorrelativasTest {

  private FiltroCorrelativas filtroCorrelativas;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    filtroCorrelativas = new FiltroCorrelativas();
  }

  private PeticionInscripcion crearPeticionInscripcionMock(boolean puedeInscribirse, boolean incluirComision) {
    PeticionInscripcion peticion = mock(PeticionInscripcion.class);
    PeticionPorMateria ppm = mock(PeticionPorMateria.class);
    Estudiante estudiante = mock(Estudiante.class);
    when(peticion.getEstudiante()).thenReturn(estudiante);

    List<PeticionPorMateria> ppmList = new ArrayList<>();
    ppmList.add(ppm);
    when(peticion.getPeticionPorMaterias()).thenReturn(ppmList);

    List<Comision> comisiones = new ArrayList<>();
    if (incluirComision) {
      Comision comision = mock(Comision.class);
      Materia materiaMock = mock(Materia.class);
      when(comision.getMateria()).thenReturn(materiaMock);
      comisiones.add(comision);

      when(estudiante.puedeInscribirse(materiaMock)).thenReturn(puedeInscribirse);
    }

    when(ppm.getComisiones()).thenReturn(comisiones);

    return peticion;
  }

  @Test
  @DisplayName("Debe devolver la petición cuando el estudiante puede inscribirse")
  void testFiltrar_conPeticionValida_devuelvePeticion() {
    List<PeticionInscripcion> peticiones = List.of(crearPeticionInscripcionMock(true, true));
    List<PeticionInscripcion> resultado = filtroCorrelativas.filtrar(peticiones);
    assertEquals(1, resultado.size());
  }

  @Test
  @DisplayName("Debe retornar lista vacía cuando no hay peticiones")
  void testFiltrar_sinPeticiones_retornaVacio() {
    List<PeticionInscripcion> resultado = filtroCorrelativas.filtrar(new ArrayList<>());
    assertTrue(resultado.isEmpty());
  }

  @Test
  @DisplayName("Debe excluir la petición si el estudiante no puede inscribirse")
  void testFiltrar_estudianteNoPuedeInscribirse_retornaVacio() {
    List<PeticionInscripcion> peticiones = List.of(crearPeticionInscripcionMock(false, true));
    List<PeticionInscripcion> resultado = filtroCorrelativas.filtrar(peticiones);
    assertTrue(resultado.isEmpty());
  }

  @Test
  @DisplayName("Debe excluir la petición si no hay comisiones asociadas a la materia")
  void testFiltrar_sinComisionesEnPeticionPorMateria() {
    List<PeticionInscripcion> peticiones = List.of(crearPeticionInscripcionMock(true, false));
    List<PeticionInscripcion> resultado = filtroCorrelativas.filtrar(peticiones);
    assertTrue(resultado.isEmpty());
  }
}
