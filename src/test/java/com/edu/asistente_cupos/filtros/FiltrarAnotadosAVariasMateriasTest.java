package com.edu.asistente_cupos.filtros;

import com.edu.asistente_cupos.domain.Comision;
import com.edu.asistente_cupos.domain.Estudiante;
import com.edu.asistente_cupos.domain.HistoriaAcademica;
import com.edu.asistente_cupos.domain.Materia;
import com.edu.asistente_cupos.domain.filtros.FiltrarAnotadosAVariasMaterias;
import com.edu.asistente_cupos.domain.peticion.PeticionInscripcion;
import com.edu.asistente_cupos.domain.peticion.PeticionPorMateria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@DisplayName("FiltrarAnotadosAVariasMateriasTest")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class FiltrarAnotadosAVariasMateriasTest {

  private FiltrarAnotadosAVariasMaterias filtro;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    filtro = new FiltrarAnotadosAVariasMaterias();
  }

  private PeticionInscripcion crearPeticionMock(int cantidadInscripcionesActuales, boolean incluirComision) {
    PeticionInscripcion peticion = mock(PeticionInscripcion.class);
    PeticionPorMateria ppm = mock(PeticionPorMateria.class);
    Estudiante estudiante = mock(Estudiante.class);
    HistoriaAcademica historia = mock(HistoriaAcademica.class);

    // Construimos un Set de comisiones mocks segun la cantidad
    Set<Materia> inscripcionesActuales = new HashSet<>();
    for (int i = 0; i < cantidadInscripcionesActuales; i++) {
      Materia materiaMock = mock(Materia.class);
      inscripcionesActuales.add(materiaMock);
    }

    when(estudiante.getHistoriaAcademica()).thenReturn(historia);
    when(historia.getInscripcionesActuales()).thenReturn(inscripcionesActuales);
    when(peticion.getEstudiante()).thenReturn(estudiante);

    List<PeticionPorMateria> ppmList = new ArrayList<>();
    ppmList.add(ppm);
    when(peticion.getPeticionPorMaterias()).thenReturn(ppmList);

    List<Comision> comisiones = new ArrayList<>();
    if (incluirComision) {
      Comision comision = mock(Comision.class);
      comisiones.add(comision);
    }
    when(ppm.getComisiones()).thenReturn(comisiones);

    return peticion;
  }

  @Test
  @DisplayName(
    "Debe incluir peticiones cuando el estudiante tiene 2 o menos inscripciones actuales")
  void testFiltrar_estudianteConHastaDosInscripciones_incluyePeticion() {
    List<PeticionInscripcion> peticiones = List.of(crearPeticionMock(0, true),
      crearPeticionMock(1, true), crearPeticionMock(2, true));
    List<PeticionInscripcion> resultado = filtro.filtrar(peticiones);
    assertEquals(3, resultado.size());
  }

  @Test
  @DisplayName("Debe excluir peticiones cuando el estudiante tiene mas de 2 inscripciones actuales")
  void testFiltrar_estudianteConMasDeDosInscripciones_excluyePeticion() {
    List<PeticionInscripcion> peticiones = List.of(crearPeticionMock(3, true),
      crearPeticionMock(4, true));
    List<PeticionInscripcion> resultado = filtro.filtrar(peticiones);
    assertTrue(resultado.isEmpty());
  }


  @Test
  @DisplayName("Debe delegar al siguiente filtro si existe")
  void testFiltrar_conFiltroSiguiente_invocaFiltroSiguiente() {
    FiltrarAnotadosAVariasMaterias filtroSiguiente = mock(FiltrarAnotadosAVariasMaterias.class);
    filtro.setFiltroSiguiente(filtroSiguiente);

    List<PeticionInscripcion> peticiones = List.of(crearPeticionMock(2, true));
    List<PeticionInscripcion> filtradas = List.of(peticiones.get(0));
    when(filtroSiguiente.filtrar(anyList())).thenReturn(filtradas);

    List<PeticionInscripcion> resultado = filtro.filtrar(peticiones);

    verify(filtroSiguiente, times(1)).filtrar(anyList());
    assertEquals(filtradas, resultado);
  }
}
