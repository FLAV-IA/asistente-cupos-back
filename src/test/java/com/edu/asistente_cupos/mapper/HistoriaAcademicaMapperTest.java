package com.edu.asistente_cupos.mapper;

import com.edu.asistente_cupos.domain.HistoriaAcademica;
import com.edu.asistente_cupos.domain.Materia;
import com.edu.asistente_cupos.domain.cursada.Cursada;
import com.edu.asistente_cupos.domain.cursada.CursadaFactory;
import com.edu.asistente_cupos.domain.prompt.optimizado.Cursada4Prompt;
import com.edu.asistente_cupos.domain.prompt.optimizado.HistoriaAcademica4Prompt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HistoriaAcademicaMapperTest {
  private HistoriaAcademicaMapper mapper;
  private CursadaMapper cursadaMapperMock;

  @BeforeEach
  void setUp() {
    cursadaMapperMock = mock(CursadaMapper.class);
    mapper = Mappers.getMapper(HistoriaAcademicaMapper.class);
    mapper.cursadaMapper = cursadaMapperMock;
  }

  @Test
  void convierteHistoriaAcademicaCorrectamente() {
    Materia mat1 = Materia.builder().codigo("MAT1").nombre("Algoritmos").build();
    Materia mat2 = Materia.builder().codigo("MAT2").nombre("Matemática").build();

    Cursada c1 = CursadaFactory.desaprobada(mat1, 2);
    Cursada c2 = CursadaFactory.enCurso(mat2);

    when(cursadaMapperMock.toCursada4Prompt(c1)).thenReturn(
      Cursada4Prompt.builder().cm("MAT1").fpm(false).build());
    when(cursadaMapperMock.toCursada4Prompt(c2)).thenReturn(
      Cursada4Prompt.builder().cm("MAT2").fpm(false).build());

    HistoriaAcademica historia = HistoriaAcademica.builder().totalInscripcionesHistoricas(10)
                                                  .totalHistoricasAprobadas(7).coeficiente(8.3)
                                                  .cursadas(List.of(c1, c2)).build();


    HistoriaAcademica4Prompt dto = mapper.toHistoriaAcademica4Prompt(historia);


    assertThat(dto.getI()).isEqualTo("10");
    assertThat(dto.getAp()).isEqualTo("7");
    assertThat(dto.getCf()).isEqualTo("8.3");

    // Solo cursadas no aprobadas van a ca
    assertThat(dto.getCa()).containsExactlyInAnyOrder("MAT1", "MAT2");

    // Solo materias en curso van a ac
    assertThat(dto.getAc()).containsExactly("MAT2");
  }
}
