
package com.edu.asistente_cupos.domain.filtros;

import com.edu.asistente_cupos.domain.Estudiante;
import com.edu.asistente_cupos.domain.HistoriaAcademica;
import com.edu.asistente_cupos.domain.peticion.PeticionInscripcion;
import com.edu.asistente_cupos.testutils.TestDataFactory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;

public class FiltroSuperposicionHorariaTest {
  @Test
  void llamaAlSiguienteFiltroSiExiste() {
    PeticionInscripcion peticion = TestDataFactory.crearPeticionInscripcionDummy();
    Estudiante estudiante = peticion.getEstudiante();
    estudiante.setHistoriaAcademica(new HistoriaAcademica());

    FiltroSuperposicionHoraria filtro = new FiltroSuperposicionHoraria();
    FiltroDePeticionInscripcion siguiente = mock(FiltroDePeticionInscripcion.class);
    when(siguiente.filtrar(any())).thenReturn(List.of(peticion));

    filtro.setFiltroSiguiente(siguiente);
    filtro.filtrar(List.of(peticion));

    verify(siguiente).filtrar(any());
  }
}
