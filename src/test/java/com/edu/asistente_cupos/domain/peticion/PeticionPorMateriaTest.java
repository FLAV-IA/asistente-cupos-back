package com.edu.asistente_cupos.domain.peticion;

import com.edu.asistente_cupos.domain.Comision;
import com.edu.asistente_cupos.domain.Materia;
import com.edu.asistente_cupos.domain.horario.HorarioParser;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PeticionPorMateriaTest {
  @Test
  void retornaMateriaDesdeLaPrimeraComision() {
    Materia materia = Materia.builder().codigo("MAT1").nombre("Matemática I").build();
    Comision comision = new Comision("C1", HorarioParser.parse("LUNES 10:00 a 12:00"), 30, materia,new ArrayList<>());

    PeticionPorMateria peticion = PeticionPorMateria.builder().comisiones(List.of(comision))
                                                    .cumpleCorrelativa(true).build();

    assertThat(peticion.getMateria()).isEqualTo(materia);
    assertThat(peticion.getCodigoMateria()).isEqualTo("MAT1");
  }

  @Test
  void lanzaExcepcionSiNoTieneComisiones() {
    PeticionPorMateria peticion = PeticionPorMateria.builder().comisiones(Collections.emptyList())
                                                    .build();

    assertThrows(IllegalStateException.class, peticion::getMateria);
  }

  @Test
  void lanzaExcepcionSiComisionesEsNull() {
    PeticionPorMateria peticion = PeticionPorMateria.builder().comisiones(null).build();

    assertThrows(IllegalStateException.class, peticion::getMateria);
  }
}