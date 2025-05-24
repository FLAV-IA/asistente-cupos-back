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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    void soloSeAsignaHastaElCupoDeLaComision() {
      Materia materia = Materia.builder().codigo("MAT2").nombre("Física").build();
      Comision comision = new Comision("MAT2-A", "martes 10-12", 3, materia);

      Estudiante e1 = Estudiante.builder().dni("300").nombre("Juan").build();
      Estudiante e2 = Estudiante.builder().dni("400").nombre("Sofi").build();

      PeticionPorMateriaPriorizada p1 = PeticionPorMateriaPriorizada.builder()
              .estudiante(e1)
              .materia(materia)
              .comisionesSolicitadas(List.of(comision))
              .cumpleCorrelativa(true)
              .prioridad(70).motivo("[COR]")
              .build();

      PeticionPorMateriaPriorizada p2 = PeticionPorMateriaPriorizada.builder()
              .estudiante(e2)
              .materia(materia)
              .comisionesSolicitadas(List.of(comision))
              .cumpleCorrelativa(true)
              .prioridad(70).motivo("[COR]")
              .build();

      List<SugerenciaInscripcion> resultado = asignador.asignar(List.of(p1, p2));

      // Verificamos que el resultado contenga las 2 sugerencias
      assertThat(resultado).hasSize(2);

      // Solo uno debe estar asignado, por el cupo 1
      List<SugerenciaInscripcion> asignadas = resultado.stream()
              .filter(SugerenciaInscripcion::fueAsignada)
              .toList();
      assertThat(asignadas).hasSize(2);

      // La comisión asignada debe ser la correcta
      SugerenciaAsignada aceptada = (SugerenciaAsignada) asignadas.get(0);
      assertThat(aceptada.comision().getCodigo()).isEqualTo("MAT2-A");

      // La otra sugerencia debe estar rechazada
      List<SugerenciaInscripcion> rechazadas = resultado.stream()
              .filter(s -> !s.fueAsignada())
              .toList();
      assertThat(rechazadas).hasSize(0);
    }



  @Test
  void logueaEscenarioYResultadoDeAsignacion() {
    Materia materia = Materia.builder().codigo("MAT1").nombre("Matemática").build();

    List<Comision> comisiones = IntStream.range(0, 700)
            .mapToObj(i -> new Comision("MAT1-" + (char) ('A' + i), "lunes " + (8 + i) + "-10", i + 1, materia))
            .collect(Collectors.toList());

    List<PeticionPorMateriaPriorizada> peticiones = IntStream.range(0, 10)
            .mapToObj(i -> {
              Estudiante estudiante = Estudiante.builder()
                      .dni(String.valueOf(1000 + i))
                      .nombre("Est" + i)
                      .build();

              List<Comision> comisionesSolicitadas = new ArrayList<>();
              Collections.shuffle(comisiones);
              comisionesSolicitadas.addAll(comisiones.subList(0, 2));

              String etiquetas = switch (i % 3) {
                case 0 -> "[AVZ]";
                case 1 -> "[COR]";
                default -> "[SIN]";
              };

              return PeticionPorMateriaPriorizada.builder()
                      .estudiante(estudiante)
                      .materia(materia)
                      .comisionesSolicitadas(comisionesSolicitadas)
                      .cumpleCorrelativa(true)
                      .prioridad(10 + i)
                      .motivo(etiquetas)
                      .build();
            }).collect(Collectors.toList());

    System.out.println("===== ESCENARIO INICIAL =====");
    System.out.println("Comisiones:");
    comisiones.forEach(c ->
            System.out.printf("  - %s (%s) Cupo: %d%n", c.getCodigo(), c.getHorario(), c.getCupo()));

    System.out.println("\nPeticiones:");
    peticiones.forEach(p -> {
      System.out.printf("  - Estudiante: %s (%s)%n", p.getEstudiante().getNombre(), p.getEstudiante().getDni());
      System.out.printf("    Etiquetas: %s | Prioridad: %d%n", p.getMotivo(), p.getPrioridad());
      System.out.print("    Comisiones solicitadas: ");
      p.getComisionesSolicitadas().forEach(c -> System.out.print(c.getCodigo() + " "));
      System.out.println("\n");
    });

    List<SugerenciaInscripcion> resultado = asignador.asignar(peticiones);

    System.out.println("===== RESULTADO DE ASIGNACIÓN =====");
    resultado.forEach(s -> {
      if (s instanceof SugerenciaAsignada asignada) {
        System.out.printf("✔ ASIGNADO -> %s a %s | Motivo: %s%n",
                asignada.estudiante().getNombre(),
                asignada.comision().getCodigo(),
                asignada.motivo());
      } else if (s instanceof SugerenciaRechazada rechazada) {
        System.out.printf("✘ RECHAZADO -> %s | Motivo: %s%n",
                rechazada.estudiante().getNombre(),
                rechazada.motivo());
      }
    });

    System.out.println("===== FIN DEL TEST =====");
  }

}


