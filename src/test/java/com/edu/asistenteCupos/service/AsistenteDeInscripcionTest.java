package com.edu.asistenteCupos.service;

import com.edu.asistenteCupos.domain.peticion.PeticionInscripcion;
import com.edu.asistenteCupos.domain.priorizacion.PeticionPorMateriaPriorizada;
import com.edu.asistenteCupos.domain.sugerencia.SugerenciaInscripcion;
import com.edu.asistenteCupos.pipeline.Paso;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.edu.asistenteCupos.testutils.TestDataFactory.crearPeticionInscripcionDummy;
import static com.edu.asistenteCupos.testutils.TestDataFactory.crearPeticionPriorizadaDummy;
import static com.edu.asistenteCupos.testutils.TestDataFactory.crearSugerenciaAsignadaDummy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AsistenteDeInscripcionTest {
  @Test
  void alPedirAlAsistenteQueSugieraInscrpcionesConLasPeticionesRetornaLasSugerenciasCorrectas() {
    Paso<List<PeticionInscripcion>, List<PeticionInscripcion>> filtro = mock(Paso.class);
    Paso<List<PeticionInscripcion>, List<PeticionPorMateriaPriorizada>> priorizador = mock(
      Paso.class);
    Paso<List<PeticionPorMateriaPriorizada>, List<SugerenciaInscripcion>> asignador = mock(
      Paso.class);
    Paso<List<SugerenciaInscripcion>, List<SugerenciaInscripcion>> traductor = mock(Paso.class);

    AsistenteDeInscripcion2 asistente = new AsistenteDeInscripcion2(filtro, priorizador, asignador,
      traductor);

    List<PeticionInscripcion> peticiones = List.of(crearPeticionInscripcionDummy());
    List<PeticionPorMateriaPriorizada> priorizadas = List.of(crearPeticionPriorizadaDummy());
    List<SugerenciaInscripcion> sugerencias = List.of(crearSugerenciaAsignadaDummy());

    when(filtro.ejecutar(peticiones)).thenReturn(peticiones);
    when(priorizador.ejecutar(peticiones)).thenReturn(priorizadas);
    when(asignador.ejecutar(priorizadas)).thenReturn(sugerencias);
    when(traductor.ejecutar(sugerencias)).thenReturn(sugerencias);


    List<SugerenciaInscripcion> resultado = asistente.sugerirInscripcion(peticiones);


    assertThat(resultado).isEqualTo(sugerencias);
    verify(filtro).ejecutar(peticiones);
    verify(priorizador).ejecutar(peticiones);
    verify(asignador).ejecutar(priorizadas);
    verify(traductor).ejecutar(sugerencias);
  }
}