package com.edu.asistenteCupos.service.asignacion.opta;

import com.edu.asistenteCupos.domain.Comision;
import com.edu.asistenteCupos.domain.Estudiante;
import com.edu.asistenteCupos.domain.Materia;
import com.edu.asistenteCupos.domain.priorizacion.PeticionPorMateriaPriorizada;
import com.edu.asistenteCupos.domain.sugerencia.SugerenciaAsignada;
import com.edu.asistenteCupos.domain.sugerencia.SugerenciaInscripcion;
import com.edu.asistenteCupos.service.asignacion.AsignadorDeCupos;
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
    Comision comision = new Comision("MAT2-A", "martes 10-12", 1, materia);

    Estudiante e1 = Estudiante.builder().dni("300").nombre("Juan").build();
    Estudiante e2 = Estudiante.builder().dni("400").nombre("Sofi").build();

    PeticionPorMateriaPriorizada p1 = PeticionPorMateriaPriorizada.builder().estudiante(e1)
                                                                  .materia(materia)
                                                                  .comisionesSolicitadas(
                                                                    List.of(comision))
                                                                  .cumpleCorrelativa(true)
                                                                  .prioridad(70).motivo("[COR]")
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
}
