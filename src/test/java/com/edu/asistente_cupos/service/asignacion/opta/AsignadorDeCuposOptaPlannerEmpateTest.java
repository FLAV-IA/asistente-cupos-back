package com.edu.asistente_cupos.service.asignacion.opta;

import com.edu.asistente_cupos.domain.Comision;
import com.edu.asistente_cupos.domain.Estudiante;
import com.edu.asistente_cupos.domain.Materia;
import com.edu.asistente_cupos.domain.horario.HorarioParser;
import com.edu.asistente_cupos.domain.priorizacion.PeticionPorMateriaPriorizada;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaAsignada;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaInscripcion;
import com.edu.asistente_cupos.service.asignacion.AsignadorDeCupos;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AsignadorDeCuposOptaPlannerEmpateTest {
  @Autowired
  AsignadorDeCupos asignador;

  @Test
  void enEmpateDePrioridadSoloUnoEsAsignado() {
    Materia materia = Materia.builder().codigo("MAT2").nombre("Física").build();
    Comision comision = new Comision("MAT2-A", HorarioParser.parse("MARTES 10:00 a 12:00"), 1,
      materia);

    Estudiante e1 = Estudiante.builder().dni("300").nombre("Juan").build();
    Estudiante e2 = Estudiante.builder().dni("400").nombre("Sofi").build();

    PeticionPorMateriaPriorizada p1 = PeticionPorMateriaPriorizada.builder().estudiante(e1)
                                                                  .materia(materia)
                                                                  .comisionesSolicitadas(
                                                                    List.of(comision))
                                                                  .cumpleCorrelativa(true)
                                                                  .prioridad(80).motivo("[COR]")
                                                                  .build();

    PeticionPorMateriaPriorizada p2 = PeticionPorMateriaPriorizada.builder().estudiante(e2)
                                                                  .materia(materia)
                                                                  .comisionesSolicitadas(
                                                                    List.of(comision))
                                                                  .cumpleCorrelativa(true)
                                                                  .prioridad(70).motivo("[COR]")
                                                                  .build();

    List<SugerenciaInscripcion> resultado = asignador.asignar(List.of(p1, p2));

    assertThat(resultado).hasSize(2);

    List<SugerenciaInscripcion> asignadas = resultado.stream()
                                                     .filter(SugerenciaInscripcion::fueAsignada)
                                                     .toList();

    assertThat(asignadas).hasSize(1);
    SugerenciaAsignada aceptada = (SugerenciaAsignada) asignadas.get(0);
    assertThat(aceptada.comision().getCodigo()).isEqualTo("MAT2-A");

    List<SugerenciaInscripcion> rechazadas = resultado.stream().filter(s -> !s.fueAsignada())
                                                      .toList();

    assertThat(rechazadas).hasSize(1);
  }

  @Test
  void noSeSuperaElCupoDisponibleDeUnaComision() {
    Materia fisica = Materia.builder().codigo("MAT2").nombre("Física").build();

    Comision unicaComision = new Comision("MAT2-A", HorarioParser.parse("LUNES 10:00 a 12:00"), 3,
      fisica);

    Estudiante juan = Estudiante.builder().dni("300").nombre("Juan").build();
    Estudiante sofi = Estudiante.builder().dni("400").nombre("Sofi").build();

    PeticionPorMateriaPriorizada peticionJuan = PeticionPorMateriaPriorizada.builder()
                                                                            .estudiante(juan)
                                                                            .materia(fisica)
                                                                            .comisionesSolicitadas(
                                                                              List.of(
                                                                                unicaComision))
                                                                            .cumpleCorrelativa(true)
                                                                            .prioridad(70)
                                                                            .motivo("[COR]")
                                                                            .build();

    PeticionPorMateriaPriorizada peticionSofi = PeticionPorMateriaPriorizada.builder()
                                                                            .estudiante(sofi)
                                                                            .materia(fisica)
                                                                            .comisionesSolicitadas(
                                                                              List.of(
                                                                                unicaComision))
                                                                            .cumpleCorrelativa(true)
                                                                            .prioridad(70)
                                                                            .motivo("[COR]")
                                                                            .build();


    List<SugerenciaInscripcion> sugerencias = asignador.asignar(
      List.of(peticionJuan, peticionSofi));


    List<SugerenciaInscripcion> asignadas = sugerencias.stream()
                                                       .filter(SugerenciaInscripcion::fueAsignada)
                                                       .toList();

    List<SugerenciaInscripcion> rechazadas = sugerencias.stream().filter(s -> !s.fueAsignada())
                                                        .toList();

    assertThat(sugerencias).hasSize(2);
    assertThat(asignadas).hasSizeLessThanOrEqualTo(unicaComision.getCupo());
    assertThat(asignadas).extracting(s -> ((SugerenciaAsignada) s).comision().getCodigo())
                         .containsOnly("MAT2-A");

    assertThat(rechazadas).hasSize(sugerencias.size() - asignadas.size());
  }
}