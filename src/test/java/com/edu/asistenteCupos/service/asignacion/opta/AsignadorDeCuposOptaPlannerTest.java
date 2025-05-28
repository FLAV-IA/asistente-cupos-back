package com.edu.asistenteCupos.service.asignacion.opta;

import com.edu.asistenteCupos.domain.Comision;
import com.edu.asistenteCupos.domain.Estudiante;
import com.edu.asistenteCupos.domain.Materia;
import com.edu.asistenteCupos.domain.priorizacion.PeticionPorMateriaPriorizada;
import com.edu.asistenteCupos.domain.sugerencia.SugerenciaAsignada;
import com.edu.asistenteCupos.domain.sugerencia.SugerenciaInscripcion;
import com.edu.asistenteCupos.domain.sugerencia.SugerenciaRechazada;
import com.edu.asistenteCupos.service.asignacion.AsignadorDeCupos;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AsignadorDeCuposOptaPlannerTest {

  @Autowired
  AsignadorDeCupos asignador;

  @Test
  void asignaCupoAlEstudianteConMayorPrioridad() {
    // Materia compartida
    Materia materia = Materia.builder().codigo("MAT1").nombre("Matemática").build();

    // Comisión con solo 1 cupo
    Comision comision = new Comision("MAT1-A", "lunes 10-12", 1, materia);

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

    List<SugerenciaInscripcion> resultado = asignador.asignar(List.of(p1, p2));

    assertThat(resultado).hasSize(2);

    long aceptadas = resultado.stream().filter(s -> s instanceof SugerenciaAsignada).count();
    long rechazadas = resultado.stream().filter(s -> s instanceof SugerenciaRechazada).count();

    assertThat(aceptadas).isEqualTo(1);
    assertThat(rechazadas).isEqualTo(1);

    SugerenciaAsignada aceptada = (SugerenciaAsignada) resultado.stream().filter(
      s -> s instanceof SugerenciaAsignada).findFirst().orElseThrow();

    assertThat(aceptada.comision().getCodigo()).isEqualTo("MAT1-A");
    assertThat(aceptada.motivo()).contains("[COR]");
  }
  @Test
  void asignaCupoAlEstudianteConEtiquetasMasValoradas() {
    // Materia compartida
    Materia materia = Materia.builder().codigo("MAT1").nombre("Matemática").build();

    // Comisión con solo 1 cupo
    Comision comision = new Comision("MAT1-A", "lunes 10-12", 1, materia);

    // Estudiantes
    Estudiante e1 = Estudiante.builder().dni("100").nombre("Ana").build();
    Estudiante e2 = Estudiante.builder().dni("200").nombre("Leo").build();

    // Peticiones:
    // Ana tiene etiquetas AVZ y COR (valen más)
    PeticionPorMateriaPriorizada p1 = PeticionPorMateriaPriorizada.builder().estudiante(e1)
            .materia(materia)
            .comisionesSolicitadas(List.of(comision))
            .cumpleCorrelativa(true)
            .prioridad(50) // prioridad base que puede ser menor que la otra
            .motivo("[AVZ], [COR]")
            .build();

    // Leo tiene solo etiqueta COR (menos peso que AVZ + COR)
    PeticionPorMateriaPriorizada p2 = PeticionPorMateriaPriorizada.builder().estudiante(e2)
            .materia(materia)
            .comisionesSolicitadas(List.of(comision))
            .cumpleCorrelativa(true)
            .prioridad(70) // mayor prioridad base pero menos etiquetas
            .motivo("[COR]")
            .build();

    List<SugerenciaInscripcion> resultado = asignador.asignar(List.of(p1, p2));

    assertThat(resultado).hasSize(2);

    long aceptadas = resultado.stream().filter(s -> s instanceof SugerenciaAsignada).count();
    long rechazadas = resultado.stream().filter(s -> s instanceof SugerenciaRechazada).count();

    assertThat(aceptadas).isEqualTo(1);
    assertThat(rechazadas).isEqualTo(1);

    SugerenciaAsignada aceptada = (SugerenciaAsignada) resultado.stream()
            .filter(s -> s instanceof SugerenciaAsignada)
            .findFirst()
            .orElseThrow();

    // Verificamos que se asignó a Ana, que tiene más etiquetas valoradas
    assertThat(aceptada.comision().getCodigo()).isEqualTo("MAT1-A");
    assertThat(aceptada.motivo()).contains("[AVZ]");
    assertThat(aceptada.motivo()).contains("[COR]");
  }
  @Test
  void noAsignaNadieCuandoNoHayCupo() {
    // Materia compartida
    Materia materia = Materia.builder().codigo("MAT1").nombre("Matemática").build();

    // Comisión sin cupo disponible
    Comision comision = new Comision("MAT1-A", "lunes 10-12", 0, materia);

    // Estudiantes
    Estudiante e1 = Estudiante.builder().dni("100").nombre("Ana").build();
    Estudiante e2 = Estudiante.builder().dni("200").nombre("Leo").build();

    // Peticiones
    PeticionPorMateriaPriorizada p1 = PeticionPorMateriaPriorizada.builder()
            .estudiante(e1)
            .materia(materia)
            .comisionesSolicitadas(List.of(comision))
            .cumpleCorrelativa(true)
            .prioridad(80)
            .motivo("[AVZ], [COR]")
            .build();

    PeticionPorMateriaPriorizada p2 = PeticionPorMateriaPriorizada.builder()
            .estudiante(e2)
            .materia(materia)
            .comisionesSolicitadas(List.of(comision))
            .cumpleCorrelativa(true)
            .prioridad(40)
            .motivo("[COR]")
            .build();

    List<SugerenciaInscripcion> resultado = asignador.asignar(List.of(p1, p2));

    assertThat(resultado).hasSize(2);

    long aceptadas = resultado.stream().filter(s -> s instanceof SugerenciaAsignada).count();
    long rechazadas = resultado.stream().filter(s -> s instanceof SugerenciaRechazada).count();

    // No debe asignar a nadie porque no hay cupo
    assertThat(aceptadas).isEqualTo(0);
    assertThat(rechazadas).isEqualTo(2);

  }


}
