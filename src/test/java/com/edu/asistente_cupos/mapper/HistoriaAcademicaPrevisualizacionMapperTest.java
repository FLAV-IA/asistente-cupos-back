package com.edu.asistente_cupos.mapper;

import com.edu.asistente_cupos.controller.dto.HistoriaAcademicaDTO;
import com.edu.asistente_cupos.domain.Estudiante;
import com.edu.asistente_cupos.domain.HistoriaAcademica;
import com.edu.asistente_cupos.domain.Materia;
import com.edu.asistente_cupos.domain.cursada.Cursada;
import com.edu.asistente_cupos.domain.cursada.CursadaFactory;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class HistoriaAcademicaPrevisualizacionMapperTest {
  private final HistoriaAcademicaPrevisualizacionMapper mapper = Mappers.getMapper(
    HistoriaAcademicaPrevisualizacionMapper.class);

  @Test
  void convierteHistoriaAcademicaADTOCorrectamente() {
    Materia matAprobada = Materia.builder().codigo("MAT1").build();
    Materia matEnCurso = Materia.builder().codigo("MAT2").build();

    Cursada cursadaAprobada = CursadaFactory.aprobada(matAprobada, 9);
    Cursada cursadaEnCurso = CursadaFactory.enCurso(matEnCurso);

    Estudiante estudiante = Estudiante.builder().dni("123").nombre("Juan").legajo("LEG-001")
                                      .build();

    HistoriaAcademica historia = HistoriaAcademica.builder().cursadas(
                                                    List.of(cursadaAprobada, cursadaEnCurso)).totalInscripcionesHistoricas(5)
                                                  .totalHistoricasAprobadas(3).coeficiente(8.0)
                                                  .estudiante(estudiante).build();


    HistoriaAcademicaDTO dto = mapper.toDto(historia);


    assertThat(dto.getDni()).isEqualTo("123");
    assertThat(dto.getCoeficiente()).isEqualTo(8.0);
    assertThat(dto.getTotalInscripcionesHistoricas()).isEqualTo(5);
    assertThat(dto.getTotalHistoricasAprobadas()).isEqualTo(3);
    assertThat(dto.getCodigosCursadasAnteriores()).containsExactly("MAT1");
    assertThat(dto.getCodigosInscripcionesActuales()).containsExactly("MAT2");
    assertThat(dto.isCumpleCorrelativas()).isTrue();
  }
}