package com.edu.asistente_cupos.mapper;

import com.edu.asistente_cupos.controller.dto.PeticionPorMateriaDTO;
import com.edu.asistente_cupos.domain.Comision;
import com.edu.asistente_cupos.domain.Materia;
import com.edu.asistente_cupos.domain.horario.HorarioParser;
import com.edu.asistente_cupos.domain.peticion.PeticionPorMateria;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PeticionPorMateriaMapperTest {
  private final PeticionPorMateriaMapper mapper = Mappers.getMapper(PeticionPorMateriaMapper.class);

  @Test
  void mapeaCorrectamentePeticionPorMateriaADTO() {
    Materia materia = Materia.builder().codigo("MAT1").nombre("Álgebra").build();

    Comision com1 = new Comision("COM1", HorarioParser.parse("LUNES 10:00 a 12:00"), 30, materia);
    Comision com2 = new Comision("COM2", HorarioParser.parse("MIERCOLES 14:00 a 16:00"), 30,
      materia);

    PeticionPorMateria peticion = PeticionPorMateria.builder().comisiones(List.of(com1, com2))
                                                    .cumpleCorrelativa(true).build();


    PeticionPorMateriaDTO dto = mapper.toDto(peticion);


    assertThat(dto.nombreMateria()).isEqualTo("Álgebra");
    assertThat(dto.codigoMateria()).isEqualTo("MAT1");
    assertThat(dto.codigosComisionesSolicitadas()).containsExactly("COM1", "COM2");
    assertThat(dto.cumpleCorrelativa()).isTrue();
  }
}
