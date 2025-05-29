package com.edu.asistente_cupos.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ComisionTest {
  @Test
  void creaComisionConBuilder() {
    Materia materia = Materia.builder().codigo("MAT101").nombre("Matemática I").build();

    Comision comision = Comision.builder().codigo("C1").horario("Lunes 10-12").cupo(30)
                                .materia(materia).build();

    assertEquals("C1", comision.getCodigo());
    assertEquals("Lunes 10-12", comision.getHorario());
    assertEquals(30, comision.getCupo());
    assertEquals(materia, comision.getMateria());
  }

  @Test
  void comisionesConMismoCodigoSonIguales() {
    Comision c1 = Comision.builder().codigo("C1").build();
    Comision c2 = Comision.builder().codigo("C1").build();

    assertEquals(c1, c2);
    assertEquals(c1.hashCode(), c2.hashCode());
  }

  @Test
  void comisionesConDistintoCodigoNoSonIguales() {
    Comision c1 = Comision.builder().codigo("C1").build();
    Comision c2 = Comision.builder().codigo("C2").build();

    assertNotEquals(c1, c2);
    assertNotEquals(c1.hashCode(), c2.hashCode());
  }

  @Test
  void equalsDebeSerFalseConObjetoDistintoONull() {
    Comision c1 = Comision.builder().codigo("C1").build();

    assertNotEquals(null, c1);
    assertNotEquals("otro tipo", c1);
  }
}
