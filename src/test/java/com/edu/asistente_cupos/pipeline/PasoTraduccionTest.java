package com.edu.asistente_cupos.pipeline;

import com.edu.asistente_cupos.domain.sugerencia.SugerenciaInscripcion;
import com.edu.asistente_cupos.service.traduccion.ConversorSugerenciasLLM;
import com.edu.asistente_cupos.service.traduccion.TraductorDeSugerencias;
import com.edu.asistente_cupos.service.traduccion.dto.SugerenciaInscripcionLLM;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.edu.asistente_cupos.testutils.TestDataFactory.crearSugerenciaAsignadaDummy;
import static com.edu.asistente_cupos.testutils.TestDataFactory.crearSugerenciaLLMDummy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PasoTraduccionTest {
  @Test
  void alEjecutarLaTraduccionDeLasSugerenciasRetornaLasSugerenciasTraducidasCorrectamente() {
    TraductorDeSugerencias traductor = mock(TraductorDeSugerencias.class);
    ConversorSugerenciasLLM conversor = mock(ConversorSugerenciasLLM.class);
    PasoTraduccion paso = new PasoTraduccion(traductor, conversor);

    List<SugerenciaInscripcion> sugerencias = List.of(crearSugerenciaAsignadaDummy());
    List<SugerenciaInscripcionLLM> sugerenciasLLM = List.of(crearSugerenciaLLMDummy());
    List<SugerenciaInscripcion> resultadoFinal = List.of(crearSugerenciaAsignadaDummy());

    when(traductor.traducir(sugerencias)).thenReturn(sugerenciasLLM);
    when(conversor.desdeLLM(sugerenciasLLM)).thenReturn(resultadoFinal);


    var resultado = paso.ejecutar(sugerencias);


    assertThat(resultado).isEqualTo(resultadoFinal);
    verify(traductor).traducir(sugerencias);
    verify(conversor).desdeLLM(sugerenciasLLM);
  }
}
