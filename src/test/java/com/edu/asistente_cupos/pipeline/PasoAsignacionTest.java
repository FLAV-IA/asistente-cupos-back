package com.edu.asistente_cupos.pipeline;

import com.edu.asistente_cupos.domain.priorizacion.PeticionPorMateriaPriorizada;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaInscripcion;
import com.edu.asistente_cupos.service.asignacion.AsignadorDeCupos;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.edu.asistente_cupos.testutils.TestDataFactory.crearPeticionPriorizadaDummy;
import static com.edu.asistente_cupos.testutils.TestDataFactory.crearSugerenciaAsignadaDummy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PasoAsignacionTest {
  @Test
  void alEjecutarConLasPeticionesPriorizadasAsignaLasSugerenciasCorrectamente() {
    AsignadorDeCupos mockAsignador = mock(AsignadorDeCupos.class);
    PasoAsignacion paso = new PasoAsignacion(mockAsignador);

    List<PeticionPorMateriaPriorizada> input = List.of(crearPeticionPriorizadaDummy());
    List<SugerenciaInscripcion> esperado = List.of(crearSugerenciaAsignadaDummy());

    when(mockAsignador.asignar(input)).thenReturn(esperado);


    var resultado = paso.ejecutar(input);


    assertThat(resultado).isEqualTo(esperado);
    verify(mockAsignador).asignar(input);
  }
}