package com.edu.asistenteCupos.pipeline;

import com.edu.asistenteCupos.domain.sugerencia.SugerenciaInscripcion;
import com.edu.asistenteCupos.service.traduccion.ConversorSugerenciasLLM;
import com.edu.asistenteCupos.service.traduccion.TraductorDeSugerencias;
import com.edu.asistenteCupos.service.traduccion.dto.SugerenciaInscripcionLLM;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.edu.asistenteCupos.testutils.TestDataFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

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
