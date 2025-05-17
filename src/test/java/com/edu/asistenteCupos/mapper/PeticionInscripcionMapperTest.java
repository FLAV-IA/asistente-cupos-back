package com.edu.asistenteCupos.mapper;

import com.edu.asistenteCupos.domain.Comision;
import com.edu.asistenteCupos.domain.HistoriaAcademica;
import com.edu.asistenteCupos.domain.Materia;
import com.edu.asistenteCupos.domain.peticion.PeticionInscripcion;
import com.edu.asistenteCupos.domain.peticion.PeticionPorMateria;
import com.edu.asistenteCupos.domain.prompt.optimizado.PeticionDeMateria4Prompt;
import com.edu.asistenteCupos.domain.prompt.optimizado.PeticionInscripcion4Prompt;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

import static com.edu.asistenteCupos.testutils.TestDataFactory.crearEstudianteDummy;
import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig
@ContextConfiguration
@Import(
  {PeticionInscripcionMapperImpl.class, HistoriaAcademicaMapperImpl.class, CursadaMapperImpl.class})
class PeticionInscripcionMapperTest {
  @Autowired
  PeticionInscripcionMapper mapper;

  @Test
  void conviertePeticionInscripcionCorrectamente() {
    Materia materia = Materia.builder().codigo("MAT1").nombre("Algoritmos").build();
    Comision comision = new Comision("C1", "Lunes 10-12", 10, materia);

    PeticionPorMateria peticionMateria = PeticionPorMateria.builder().comisiones(List.of(comision))
                                                           .cumpleCorrelativa(true).build();

    HistoriaAcademica historia = HistoriaAcademica.builder().totalInscripcionesHistoricas(5)
                                                  .totalHistoricasAprobadas(3).coeficiente(7.5)
                                                  .build();

    var estudiante = crearEstudianteDummy("12345678");
    estudiante.setHistoriaAcademica(historia);

    PeticionInscripcion peticion = PeticionInscripcion.builder().estudiante(estudiante)
                                                      .peticionPorMaterias(List.of(peticionMateria))
                                                      .build();

    PeticionInscripcion4Prompt resultado = mapper.toPeticionInscripcion4Prompt(peticion);

    assertThat(resultado.getA()).isEqualTo("12345678");
    assertThat(resultado.getH()).isNotNull();
    assertThat(resultado.getP()).hasSize(1);

    PeticionDeMateria4Prompt pm = resultado.getP().get(0);
    assertThat(pm.getN()).isEqualTo("MAT1");
    assertThat(pm.getM()).containsExactly("C1");
    assertThat(pm.isC()).isTrue();
  }
}
