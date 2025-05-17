package com.edu.asistenteCupos.domain.peticion;

import com.edu.asistenteCupos.domain.Comision;
import com.edu.asistenteCupos.domain.Materia;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PeticionPorMateriaTest {
  @Test
  void retornaMateriaDesdeLaPrimeraComision() {
    Materia materia = Materia.builder().codigo("MAT1").nombre("Matemática I").build();
    Comision comision = new Comision("C1", "Lunes 10-12", 30, materia);

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