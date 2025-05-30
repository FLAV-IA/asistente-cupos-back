package com.edu.asistente_cupos.service;

import com.edu.asistente_cupos.domain.peticion.PeticionInscripcion;
import com.edu.asistente_cupos.domain.priorizacion.PeticionPorMateriaPriorizada;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaInscripcion;
import com.edu.asistente_cupos.observacion.NombresMetricas;
import com.edu.asistente_cupos.observacion.TimeTracker;
import com.edu.asistente_cupos.pipeline.Paso;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.Callable;

import static com.edu.asistente_cupos.testutils.TestDataFactory.crearPeticionInscripcionDummy;
import static com.edu.asistente_cupos.testutils.TestDataFactory.crearPeticionPriorizadaDummy;
import static com.edu.asistente_cupos.testutils.TestDataFactory.crearSugerenciaAsignadaDummy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AsistenteDeInscripcionTest {
  @Test
  void alPedirAlAsistenteQueSugieraInscripcionesConLasPeticionesRetornaLasSugerenciasCorrectas() throws Exception {
    Paso<List<PeticionInscripcion>, List<PeticionInscripcion>> filtro = mock(Paso.class);
    Paso<List<PeticionInscripcion>, List<PeticionPorMateriaPriorizada>> priorizador = mock(
      Paso.class);
    Paso<List<PeticionPorMateriaPriorizada>, List<SugerenciaInscripcion>> asignador = mock(
      Paso.class);
    Paso<List<SugerenciaInscripcion>, List<SugerenciaInscripcion>> traductor = mock(Paso.class);
    TimeTracker tracker = mock(TimeTracker.class);

    AsistenteDeInscripcion asistente = new AsistenteDeInscripcion(filtro, priorizador, asignador,
      traductor, tracker);

    List<PeticionInscripcion> peticiones = List.of(crearPeticionInscripcionDummy());
    List<PeticionPorMateriaPriorizada> priorizadas = List.of(crearPeticionPriorizadaDummy());
    List<SugerenciaInscripcion> sugerencias = List.of(crearSugerenciaAsignadaDummy());

    when(filtro.ejecutar(peticiones)).thenReturn(peticiones);
    when(priorizador.ejecutar(peticiones)).thenReturn(priorizadas);
    when(asignador.ejecutar(priorizadas)).thenReturn(sugerencias);
    when(traductor.ejecutar(sugerencias)).thenReturn(sugerencias);

    when(tracker.track(anyString(), any(Callable.class))).thenAnswer(invocation -> {
      Callable<?> callable = invocation.getArgument(1);
      return callable.call();
    });


    List<SugerenciaInscripcion> resultado = asistente.sugerirInscripcion(peticiones);


    assertThat(resultado).isEqualTo(sugerencias);
    verify(filtro).ejecutar(peticiones);
    verify(priorizador).ejecutar(peticiones);
    verify(asignador).ejecutar(priorizadas);
    verify(traductor).ejecutar(sugerencias);
    verify(tracker).track(eq(NombresMetricas.ASISTENTE_SUGERENCIA_TOTAL), any());
  }
}