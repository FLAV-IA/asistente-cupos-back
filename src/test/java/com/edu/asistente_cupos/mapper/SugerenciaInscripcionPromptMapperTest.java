package com.edu.asistente_cupos.mapper;

import com.edu.asistente_cupos.domain.prompt.optimizado.SugerenciaParaTraducir4Prompt;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaAsignada;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaInscripcion;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.edu.asistente_cupos.testutils.TestDataFactory.crearSugerenciaAsignadaDummy;
import static com.edu.asistente_cupos.testutils.TestDataFactory.crearSugerenciaRechazadaDummy;
import static org.assertj.core.api.Assertions.assertThat;

class SugerenciaInscripcionPromptMapperTest {

  private final SugerenciaInscripcionPromptMapper mapper = new SugerenciaInscripcionPromptMapper();

  @Test
  void mapeaSugerenciaAsignadaCorrectamente() {
    SugerenciaInscripcion asignada = crearSugerenciaAsignadaDummy();

    List<SugerenciaParaTraducir4Prompt> resultado = mapper.toPromptList(List.of(asignada));

    assertThat(resultado).hasSize(1);
    var prompt = resultado.get(0);
    assertThat(prompt.getA()).isEqualTo(asignada.estudiante().getDni());
    assertThat(prompt.getM()).isEqualTo(((SugerenciaAsignada) asignada).comision().getCodigo());
    assertThat(prompt.isX()).isTrue();
    assertThat(prompt.getP()).isEqualTo(asignada.prioridad());
    assertThat(prompt.getE()).isEqualTo(asignada.motivo());
  }

  @Test
  void mapeaSugerenciaRechazadaCorrectamente() {
    SugerenciaInscripcion rechazada = crearSugerenciaRechazadaDummy();

    List<SugerenciaParaTraducir4Prompt> resultado = mapper.toPromptList(List.of(rechazada));

    assertThat(resultado).hasSize(1);
    var prompt = resultado.get(0);
    assertThat(prompt.getA()).isEqualTo(rechazada.estudiante().getDni());
    assertThat(prompt.isX()).isFalse();
    assertThat(prompt.getP()).isEqualTo(rechazada.prioridad());
    assertThat(prompt.getE()).isEqualTo(rechazada.motivo());
  }
}
