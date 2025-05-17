package com.edu.asistenteCupos.pipeline;

import com.edu.asistenteCupos.domain.priorizacion.PeticionPorMateriaPriorizada;
import com.edu.asistenteCupos.domain.sugerencia.SugerenciaInscripcion;
import com.edu.asistenteCupos.service.asignacion.AsignadorDeCupos;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.edu.asistenteCupos.testutils.TestDataFactory.crearPeticionPriorizadaDummy;
import static com.edu.asistenteCupos.testutils.TestDataFactory.crearSugerenciaAsignadaDummy;
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