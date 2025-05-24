package com.edu.asistenteCupos.mapper;

import com.edu.asistenteCupos.domain.Comision;
import com.edu.asistenteCupos.domain.Cursada;
import com.edu.asistenteCupos.domain.HistoriaAcademica;
import com.edu.asistenteCupos.domain.Materia;
import com.edu.asistenteCupos.domain.prompt.optimizado.Cursada4Prompt;
import com.edu.asistenteCupos.domain.prompt.optimizado.HistoriaAcademica4Prompt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

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
    Comision comision = Comision.builder().codigo("COM1").build();

    Cursada c1 = Cursada.builder().id(1L).materia(mat1).fueAprobada(false).build();
    Cursada c2 = Cursada.builder().id(2L).materia(mat2).fueAprobada(true).build();

    when(cursadaMapperMock.toCursada4Prompt(c1))
      .thenReturn(Cursada4Prompt.builder().cm("MAT1").build());

    HistoriaAcademica historia = HistoriaAcademica.builder()
                                                  .totalInscripcionesHistoricas(10)
                                                  .totalHistoricasAprobadas(7)
                                                  .coeficiente(8.3)
                                                  .cursadasAnteriores(List.of(c1, c2))
                                                  .inscripcionesActuales(Set.of(mat2))
                                                  .build();

    HistoriaAcademica4Prompt dto = mapper.toHistoriaAcademica4Prompt(historia);

    assertThat(dto.getI()).isEqualTo("10");
    assertThat(dto.getAp()).isEqualTo("7");
    assertThat(dto.getCf()).isEqualTo("8.3");
    assertThat(dto.getCa()).containsExactly("MAT1");
    assertThat(dto.getAc()).containsExactly("MAT2");
  }
}
