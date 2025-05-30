package com.edu.asistente_cupos.mapper;

import com.edu.asistente_cupos.domain.Materia;
import com.edu.asistente_cupos.domain.cursada.Cursada;
import com.edu.asistente_cupos.domain.cursada.CursadaFactory;
import com.edu.asistente_cupos.domain.prompt.optimizado.Cursada4Prompt;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class CursadaMapperTest {
  private final CursadaMapper mapper = Mappers.getMapper(CursadaMapper.class);

  @Test
  void mapeaCursadaCorrectamente() {
    Materia materia = Materia.builder().codigo("MAT1").nombre("Matemática").build();
    Cursada cursada = CursadaFactory.aprobada(materia, 10);

    Cursada4Prompt resultado = mapper.toCursada4Prompt(cursada);

    assertThat(resultado.getCm()).isEqualTo("MAT1");
    assertThat(resultado.getFpm()).isTrue();
  }

  @Test
  void mapeaCursadaNoAprobadaCorrectamente() {
    Materia materia = Materia.builder().codigo("MAT2").nombre("Física").build();
    Cursada cursada = CursadaFactory.desaprobada(materia, 2);

    Cursada4Prompt resultado = mapper.toCursada4Prompt(cursada);

    assertThat(resultado.getCm()).isEqualTo("MAT2");
    assertThat(resultado.getFpm()).isFalse();
  }
}
