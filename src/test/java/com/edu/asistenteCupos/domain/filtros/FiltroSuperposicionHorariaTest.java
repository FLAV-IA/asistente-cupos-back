
package com.edu.asistenteCupos.domain.filtros;

import com.edu.asistenteCupos.domain.Comision;
import com.edu.asistenteCupos.domain.Estudiante;
import com.edu.asistenteCupos.domain.HistoriaAcademica;
import com.edu.asistenteCupos.domain.peticion.PeticionInscripcion;
import com.edu.asistenteCupos.testutils.TestDataFactory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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
