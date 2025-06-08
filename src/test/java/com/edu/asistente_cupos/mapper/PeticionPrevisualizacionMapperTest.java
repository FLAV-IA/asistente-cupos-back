package com.edu.asistente_cupos.mapper;

import com.edu.asistente_cupos.controller.dto.PeticionInscripcionDTO;
import com.edu.asistente_cupos.controller.dto.PeticionPorMateriaDTO;
import com.edu.asistente_cupos.domain.Comision;
import com.edu.asistente_cupos.domain.Estudiante;
import com.edu.asistente_cupos.domain.HistoriaAcademica;
import com.edu.asistente_cupos.domain.Materia;
import com.edu.asistente_cupos.domain.horario.HorarioParser;
import com.edu.asistente_cupos.domain.peticion.PeticionInscripcion;
import com.edu.asistente_cupos.domain.peticion.PeticionPorMateria;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PeticionPrevisualizacionMapperTest {
  @Autowired
  PeticionPrevisualizacionMapper mapper;

  @Test
  void convierteCorrectamenteUnaPeticionAUnDTO() {
    Materia materia = Materia.builder().codigo("MAT1").nombre("Álgebra").build();
    Comision comision = new Comision("C1", HorarioParser.parse("LUNES 09:00 a 11:00"), 30, materia);

    PeticionPorMateria peticionPorMateria = PeticionPorMateria.builder()
                                                              .comisiones(List.of(comision))
                                                              .cumpleCorrelativa(true).build();

    HistoriaAcademica historia = HistoriaAcademica.builder().coeficiente(8.5)
                                                  .totalHistoricasAprobadas(10)
                                                  .totalInscripcionesHistoricas(12).build();

    Estudiante estudiante = Estudiante.builder().nombre("Juan").dni("12345678")
                                      .historiaAcademica(historia).build();

    PeticionInscripcion peticion = PeticionInscripcion.builder().estudiante(estudiante)
                                                      .peticionPorMaterias(
                                                        List.of(peticionPorMateria)).build();


    PeticionInscripcionDTO dto = mapper.toDto(peticion);


    assertThat(dto.nombre()).isEqualTo("Juan");
    assertThat(dto.dni()).isEqualTo("12345678");
    assertThat(dto.historiaAcademica()).isNotNull();
    assertThat(dto.historiaAcademica().getCoeficiente()).isEqualTo(8.5);
    assertThat(dto.materias()).hasSize(1);

    PeticionPorMateriaDTO dtoMateria = dto.materias().get(0);
    assertThat(dtoMateria.codigoMateria()).isEqualTo("MAT1");
    assertThat(dtoMateria.nombreMateria()).isEqualTo("Álgebra");
    assertThat(dtoMateria.codigosComisionesSolicitadas()).containsExactly("C1");
    assertThat(dtoMateria.cumpleCorrelativa()).isTrue();
  }
}
