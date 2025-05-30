package com.edu.asistente_cupos.pipeline;

import com.edu.asistente_cupos.domain.filtros.FiltroDePeticionInscripcion;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.edu.asistente_cupos.testutils.TestDataFactory.crearPeticionInscripcionDummy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PasoFiltradoDePeticionesTest {
  @Test
  void alEjecutarConLasPeticionesYUnFiltroRetornaLasPeticionesFiltradas() {
    var mockFiltro = mock(FiltroDePeticionInscripcion.class);
    var paso = new PasoFiltradoDePeticiones(mockFiltro);

    var input = List.of(crearPeticionInscripcionDummy(), crearPeticionInscripcionDummy());
    var filtradas = List.of(crearPeticionInscripcionDummy());

    when(mockFiltro.filtrar(input)).thenReturn(filtradas);


    var resultado = paso.ejecutar(input);


    assertThat(resultado).isEqualTo(filtradas);
    verify(mockFiltro).filtrar(input);
  }
}