package com.edu.asistenteCupos.utils;

import com.edu.asistenteCupos.Utils.JsonConverter;
import com.edu.asistenteCupos.domain.Estudiante;
import com.edu.asistenteCupos.domain.Materia;
import com.edu.asistenteCupos.domain.PeticionInscripcion;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JsonConvertertTest {
  @Test
  void conviertePeticionInscripcionAJson() {

    Estudiante estudiante = Estudiante.builder().nombre("Tomas").dni("7890").build();
    Materia materia = Materia.builder().codigo("MAT01").build();
    PeticionInscripcion peticion = PeticionInscripcion.builder().materia(materia)
                                                      .cumpleCorrelativa(true)
                                                      .estudiante(estudiante).build();

    String json = JsonConverter.toJson(peticion);

    assertThat(json).contains("Tomas");
    assertThat(json).contains("7890");
    assertThat(json).contains("MAT01");
  }

  @Test
  void lanzaUnErrorSiFallaLaSerializacionAJson() {
    PeticionInscripcion peticion = mock(PeticionInscripcion.class);
    when(peticion.getMateria()).thenThrow(new RuntimeException("Fallo interno"));

    RuntimeException ex = assertThrows(RuntimeException.class,
      () -> JsonConverter.toJson(peticion));
    assertThat(ex.getMessage()).contains("Error al convertir a JSON");
  }
}
