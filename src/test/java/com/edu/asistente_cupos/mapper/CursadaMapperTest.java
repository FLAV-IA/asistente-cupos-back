package com.edu.asistente_cupos.mapper;

import com.edu.asistente_cupos.domain.Cursada;
import com.edu.asistente_cupos.domain.Materia;
import com.edu.asistente_cupos.domain.prompt.optimizado.Cursada4Prompt;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class CursadaMapperTest {
  private final CursadaMapper mapper = Mappers.getMapper(CursadaMapper.class);

  @Test
  void mapeaCursadaCorrectamente() {
    Materia materia = Materia.builder().codigo("MAT1").nombre("Matemática").build();

    Cursada cursada = Cursada.builder().id(1L).materia(materia).fueAprobada(true).build();

    Cursada4Prompt resultado = mapper.toCursada4Prompt(cursada);

    assertThat(resultado.getCm()).isEqualTo("MAT1");
    assertThat(resultado.getFpm()).isTrue();
  }

  @Test
  void mapeaCursadaNoAprobadaCorrectamente() {
    Materia materia = Materia.builder().codigo("MAT2").nombre("Física").build();

    Cursada cursada = Cursada.builder().id(2L).materia(materia).fueAprobada(false).build();

    Cursada4Prompt resultado = mapper.toCursada4Prompt(cursada);

    assertThat(resultado.getCm()).isEqualTo("MAT2");
    assertThat(resultado.getFpm()).isFalse();
  }
}
