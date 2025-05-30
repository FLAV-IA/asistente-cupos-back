package com.edu.asistente_cupos.pipeline;

import com.edu.asistente_cupos.domain.priorizacion.PeticionPorMateriaPriorizada;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaInscripcion;
import com.edu.asistente_cupos.observacion.TimeTracker;
import com.edu.asistente_cupos.service.asignacion.AsignadorDeCupos;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.Callable;

import static com.edu.asistente_cupos.testutils.TestDataFactory.crearPeticionPriorizadaDummy;
import static com.edu.asistente_cupos.testutils.TestDataFactory.crearSugerenciaAsignadaDummy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PasoAsignacionTest {
  @Test
  void alEjecutarConLasPeticionesPriorizadasAsignaLasSugerenciasCorrectamente() {
    AsignadorDeCupos mockAsignador = mock(AsignadorDeCupos.class);
    TimeTracker mockTracker = mock(TimeTracker.class);
    PasoAsignacion paso = new PasoAsignacion(mockAsignador, mockTracker);

    List<PeticionPorMateriaPriorizada> input = List.of(crearPeticionPriorizadaDummy());
    List<SugerenciaInscripcion> esperado = List.of(crearSugerenciaAsignadaDummy());

    when(mockAsignador.asignar(input)).thenReturn(esperado);

    when(mockTracker.track(anyString(), any(Callable.class))).thenAnswer(invocation -> {
      Callable<?> callable = invocation.getArgument(1);
      return callable.call();
    });


    var resultado = paso.ejecutar(input);


    assertThat(resultado).isEqualTo(esperado);
    verify(mockAsignador).asignar(input);
    verify(mockTracker).track(eq("pipeline.asignacion.total"), any(Callable.class));
  }
}