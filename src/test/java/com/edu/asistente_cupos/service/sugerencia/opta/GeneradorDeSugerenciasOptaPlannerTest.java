package com.edu.asistente_cupos.service.sugerencia.opta;

import com.edu.asistente_cupos.domain.Comision;
import com.edu.asistente_cupos.domain.Estudiante;
import com.edu.asistente_cupos.domain.Materia;
import com.edu.asistente_cupos.domain.horario.HorarioParser;
import com.edu.asistente_cupos.domain.priorizacion.PeticionPorMateriaPriorizada;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaAceptada;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaInscripcion;
import com.edu.asistente_cupos.service.sugerencia.GeneradorDeSugerenciasDeCupos;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class GeneradorDeSugerenciasOptaPlannerTest {

  @Autowired
  GeneradorDeSugerenciasDeCupos asignador;

  @Test
  void asignaCupoAlEstudianteConMayorPrioridad() {
    // Materia compartida
    Materia materia = Materia.builder().codigo("MAT1").nombre("Matemática").build();

    // Comisión con solo 1 cupo
    Comision comision = new Comision("MAT1-A", HorarioParser.parse("LUNES 10:00 a 12:00"), 1,
      materia);

    // Estudiantes
    Estudiante e1 = Estudiante.builder().dni("100").nombre("Ana").build();
    Estudiante e2 = Estudiante.builder().dni("200").nombre("Leo").build();

    // Peticiones
    PeticionPorMateriaPriorizada p1 = PeticionPorMateriaPriorizada.builder().estudiante(e1)
                                                                  .materia(materia)
                                                                  .comisionesSolicitadas(
                                                                    List.of(comision))
                                                                  .cumpleCorrelativa(true)
                                                                  .prioridad(80)
                                                                  .motivo("[COR], [AVZ]").build();

    PeticionPorMateriaPriorizada p2 = PeticionPorMateriaPriorizada.builder().estudiante(e2)
                                                                  .materia(materia)
                                                                  .comisionesSolicitadas(
                                                                    List.of(comision))
                                                                  .cumpleCorrelativa(true)
                                                                  .prioridad(40).motivo("[COR]")
                                                                  .build();

    List<SugerenciaInscripcion> resultado = asignador.sugerir(List.of(p1, p2));

    assertThat(resultado).hasSize(2);

    long aceptadas = resultado.stream().filter(SugerenciaInscripcion::fueAceptada).count();
    long rechazadas = resultado.stream().filter(s -> !s.fueAceptada()).count();

    assertThat(aceptadas).isEqualTo(1);
    assertThat(rechazadas).isEqualTo(1);

    SugerenciaAceptada aceptada = (SugerenciaAceptada) resultado.stream().filter(
      SugerenciaInscripcion::fueAceptada).findFirst().orElseThrow();

    assertThat(aceptada.comision().getCodigo()).isEqualTo("MAT1-A");
    assertThat(aceptada.motivo()).contains("[COR]");
  }

  @Test
  void noAsignaNadieCuandoNoHayCupo() {
    // Materia compartida
    Materia materia = Materia.builder().codigo("MAT1").nombre("Matemática").build();

    // Comisión sin cupo disponible
    Comision comision = new Comision("MAT1-A", HorarioParser.parse("LUNES 10:00 a 12:00"), 0,
      materia);

    // Estudiantes
    Estudiante e1 = Estudiante.builder().dni("100").nombre("Ana").build();
    Estudiante e2 = Estudiante.builder().dni("200").nombre("Leo").build();

    // Peticiones
    PeticionPorMateriaPriorizada p1 = PeticionPorMateriaPriorizada.builder().estudiante(e1)
                                                                  .materia(materia)
                                                                  .comisionesSolicitadas(
                                                                    List.of(comision))
                                                                  .cumpleCorrelativa(true)
                                                                  .prioridad(80)
                                                                  .motivo("[AVZ], [COR]").build();

    PeticionPorMateriaPriorizada p2 = PeticionPorMateriaPriorizada.builder().estudiante(e2)
                                                                  .materia(materia)
                                                                  .comisionesSolicitadas(
                                                                    List.of(comision))
                                                                  .cumpleCorrelativa(true)
                                                                  .prioridad(40).motivo("[COR]")
                                                                  .build();

    List<SugerenciaInscripcion> resultado = asignador.sugerir(List.of(p1, p2));

    assertThat(resultado).hasSize(2);

    long aceptadas = resultado.stream().filter(SugerenciaInscripcion::fueAceptada).count();
    long rechazadas = resultado.stream().filter(s -> !s.fueAceptada()).count();

    // No debe asignar a nadie porque no hay cupo
    assertThat(aceptadas).isZero();
    assertThat(rechazadas).isEqualTo(2);
  }
}
