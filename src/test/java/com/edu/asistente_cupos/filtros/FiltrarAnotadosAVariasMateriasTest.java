package com.edu.asistente_cupos.filtros;

import com.edu.asistente_cupos.domain.Comision;
import com.edu.asistente_cupos.domain.Estudiante;
import com.edu.asistente_cupos.domain.HistoriaAcademica;
import com.edu.asistente_cupos.domain.Materia;
import com.edu.asistente_cupos.domain.cursada.Cursada;
import com.edu.asistente_cupos.domain.cursada.EstadoCursadaEnCurso;
import com.edu.asistente_cupos.domain.filtros.FiltrarAnotadosAVariasMaterias;
import com.edu.asistente_cupos.domain.peticion.PeticionInscripcion;
import com.edu.asistente_cupos.domain.peticion.PeticionPorMateria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class FiltrarAnotadosAVariasMateriasTest {
  private FiltrarAnotadosAVariasMaterias filtro;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    filtro = new FiltrarAnotadosAVariasMaterias();
  }

  @Test
  void incluyePeticionesSiTieneHastaDosCursadasEnCurso() {
    List<PeticionInscripcion> peticiones = List.of(crearPeticionMock(0), crearPeticionMock(1),
      crearPeticionMock(2));


    List<PeticionInscripcion> resultado = filtro.filtrar(peticiones);


    assertEquals(3, resultado.size());
  }

  @Test
  void excluyePeticionesSiTieneMasDeDosCursadasEnCurso() {
    List<PeticionInscripcion> peticiones = List.of(crearPeticionMock(3), crearPeticionMock(4));


    List<PeticionInscripcion> resultado = filtro.filtrar(peticiones);


    assertTrue(resultado.isEmpty());
  }

  @Test
  void delegaAlSiguienteFiltroSiExiste() {
    FiltrarAnotadosAVariasMaterias siguienteFiltro = mock(FiltrarAnotadosAVariasMaterias.class);
    filtro.setFiltroSiguiente(siguienteFiltro);

    List<PeticionInscripcion> peticiones = List.of(crearPeticionMock(2));
    List<PeticionInscripcion> filtradas = List.of(peticiones.get(0));

    when(siguienteFiltro.filtrar(anyList())).thenReturn(filtradas);


    List<PeticionInscripcion> resultado = filtro.filtrar(peticiones);


    verify(siguienteFiltro, times(1)).filtrar(anyList());
    assertEquals(filtradas, resultado);
  }

  private PeticionInscripcion crearPeticionMock(int cantidadCursadasEnCurso) {
    Materia materiaDummy = Materia.builder().codigo("M1").build();
    List<Comision> comisiones = new ArrayList<>();

    Comision comision = mock(Comision.class);
    when(comision.getMateria()).thenReturn(materiaDummy);
    comisiones.add(comision);

    PeticionPorMateria ppm = PeticionPorMateria.builder().comisiones(comisiones)
                                               .cumpleCorrelativa(true).build();

    List<PeticionPorMateria> ppmList = List.of(ppm);

    Estudiante estudiante = mock(Estudiante.class);
    HistoriaAcademica historia = mock(HistoriaAcademica.class);
    List<Cursada> cursadas = new ArrayList<>();

    for (int i = 0; i < cantidadCursadasEnCurso; i++) {
      Materia materia = Materia.builder().codigo("MAT" + i).build();
      Cursada cursada = mock(Cursada.class);
      when(cursada.getEstado()).thenReturn(new EstadoCursadaEnCurso());
      when(cursada.getMateria()).thenReturn(materia);
      cursadas.add(cursada);
    }

    when(historia.getCursadas()).thenReturn(cursadas);
    when(estudiante.getHistoriaAcademica()).thenReturn(historia);

    when(estudiante.estaInscriptoEnMasDe(anyInt())).thenAnswer(invocation -> {
      int cantidad = invocation.getArgument(0);
      return cantidadCursadasEnCurso > cantidad;
    });

    return PeticionInscripcion.builder().estudiante(estudiante).peticionPorMaterias(ppmList)
                              .build();
  }
}