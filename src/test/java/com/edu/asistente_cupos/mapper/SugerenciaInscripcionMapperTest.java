package com.edu.asistente_cupos.mapper;

import com.edu.asistente_cupos.controller.dto.SugerenciaInscripcionDTO;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaInscripcion;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.edu.asistente_cupos.testutils.TestDataFactory.crearSugerenciaAsignadaDummy;
import static com.edu.asistente_cupos.testutils.TestDataFactory.crearSugerenciaRechazadaDummy;
import static org.assertj.core.api.Assertions.assertThat;

class SugerenciaInscripcionMapperTest {
  private final SugerenciaInscripcionMapper mapper = new SugerenciaInscripcionMapper();

  @Test
  void convierteSugerenciaAsignadaADtoCorrectamente() {
    var sugerencia = crearSugerenciaAsignadaDummy();

    SugerenciaInscripcionDTO dto = mapper.toDto(sugerencia);

    assertThat(dto.getNombreEstudiante()).isEqualTo(sugerencia.estudiante().getNombre());
    assertThat(dto.getDniEstudiante()).isEqualTo(sugerencia.estudiante().getDni());
    assertThat(dto.getNombreMateria()).isEqualTo(sugerencia.materia().getNombre());
    assertThat(dto.getMotivo()).isEqualTo(sugerencia.motivo());
    assertThat(dto.getPrioridad()).isEqualTo(sugerencia.prioridad());
    assertThat(dto.isCupoAsignado()).isTrue();
  }

  @Test
  void convierteSugerenciaRechazadaADtoCorrectamente() {
    var sugerencia = crearSugerenciaRechazadaDummy();

    SugerenciaInscripcionDTO dto = mapper.toDto(sugerencia);

    assertThat(dto.getNombreEstudiante()).isEqualTo(sugerencia.estudiante().getNombre());
    assertThat(dto.getDniEstudiante()).isEqualTo(sugerencia.estudiante().getDni());
    assertThat(dto.getNombreMateria()).isEqualTo(sugerencia.materia().getNombre());
    assertThat(dto.getMotivo()).isEqualTo(sugerencia.motivo());
    assertThat(dto.getPrioridad()).isEqualTo(sugerencia.prioridad());
    assertThat(dto.isCupoAsignado()).isFalse();
  }

  @Test
  void convierteListaDeSugerenciasADtosCorrectamente() {
    List<SugerenciaInscripcion> sugerencias = List.of(crearSugerenciaAsignadaDummy(),
      crearSugerenciaRechazadaDummy());

    List<SugerenciaInscripcionDTO> dtos = mapper.toSugerenciaInscripcionDtoList(sugerencias);

    assertThat(dtos).hasSize(2);
    assertThat(dtos.get(0).isCupoAsignado()).isTrue();
    assertThat(dtos.get(1).isCupoAsignado()).isFalse();
  }
}
