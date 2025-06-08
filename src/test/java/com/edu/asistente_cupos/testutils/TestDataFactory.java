package com.edu.asistente_cupos.testutils;

import com.edu.asistente_cupos.domain.Comision;
import com.edu.asistente_cupos.domain.Estudiante;
import com.edu.asistente_cupos.domain.HistoriaAcademica;
import com.edu.asistente_cupos.domain.Materia;
import com.edu.asistente_cupos.domain.horario.HorarioParser;
import com.edu.asistente_cupos.domain.peticion.PeticionInscripcion;
import com.edu.asistente_cupos.domain.peticion.PeticionPorMateria;
import com.edu.asistente_cupos.domain.priorizacion.PeticionPorMateriaPriorizada;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaAceptada;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaInscripcion;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaRechazada;
import com.edu.asistente_cupos.service.priorizacion.dto.ResultadoPriorizacionLLM;
import com.edu.asistente_cupos.service.traduccion.dto.SugerenciaInscripcionLLM;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;

import java.util.List;

public class TestDataFactory {
  public static Estudiante crearEstudianteDummy() {
    return crearEstudianteDummy("12345678");
  }

  public static Materia crearMateriaDummy() {
    return crearMateriaDummy("MAT1", "Matemática I");
  }

  public static Materia crearMateriaDummy(String codigo, String nombre) {
    return Materia.builder().codigo(codigo).nombre(nombre).build();
  }

  public static Comision crearComisionDummy() {
    return crearComisionDummy("C1", 30, crearMateriaDummy());
  }

  public static Comision crearComisionDummy(String codigo, int cupo, Materia materia) {
    return new Comision(codigo, HorarioParser.parse("LUNES 09:00 a 10:00"), cupo, materia);
  }

  public static PeticionPorMateriaPriorizada crearPeticionPriorizadaDummy() {
    return PeticionPorMateriaPriorizada.builder().estudiante(crearEstudianteDummy())
                                       .materia(crearMateriaDummy())
                                       .comisionesSolicitadas(List.of(crearComisionDummy()))
                                       .cumpleCorrelativa(true).prioridad(1).motivo("COR").build();
  }

  public static ResultadoPriorizacionLLM crearResultadoPriorizacionLLMDummy() {
    ResultadoPriorizacionLLM.EvaluacionPrioridad evaluacion = new ResultadoPriorizacionLLM.EvaluacionPrioridad();
    evaluacion.setN("MAT101");
    evaluacion.setP(91);
    evaluacion.setE(List.of("COR", "AVZ"));

    return new ResultadoPriorizacionLLM("12345678", List.of(evaluacion));
  }

  public static SugerenciaInscripcion crearSugerenciaAsignadaDummy() {
    return new SugerenciaAceptada(crearEstudianteDummy(), crearMateriaDummy(), crearComisionDummy(),
      "Asignada [COR]", 1);
  }

  public static SugerenciaInscripcion crearSugerenciaRechazadaDummy() {
    return new SugerenciaRechazada(crearEstudianteDummy(), crearMateriaDummy(),
      crearComisionDummy(), "Rechazada por falta de cupo", 2);
  }

  public static SugerenciaInscripcionLLM crearSugerenciaLLMDummy() {
    SugerenciaInscripcionLLM sugerencia = new SugerenciaInscripcionLLM();
    sugerencia.setA("12345678");
    sugerencia.setC("C1");
    sugerencia.setP(1);
    sugerencia.setM("Asignada [COR]");
    sugerencia.setX(true);
    return sugerencia;
  }

  public static Estudiante crearEstudianteDummy(String dni) {
    Estudiante estudiante = Estudiante.builder().dni(dni).legajo("LEG" + dni).nombre("Juan")
                                      .mail(dni + "@test.com").build();

    HistoriaAcademica historia = HistoriaAcademica.builder().estudiante(estudiante)
                                                  .totalInscripcionesHistoricas(1)
                                                  .totalHistoricasAprobadas(1).coeficiente(8.0)
                                                  .cursadas(List.of()) // vacías por ahora
                                                  .build();

    estudiante.setHistoriaAcademica(historia);
    return estudiante;
  }

  public static PeticionInscripcion crearPeticionInscripcionParaPipeline() {
    Estudiante estudiante = crearEstudianteDummy("45072112");

    Materia materia = Materia.builder().codigo("1041").nombre("Matemática II").build();

    Comision comision = Comision.builder().codigo("1041-1-G14").materia(materia)
                                .horario(HorarioParser.parse("Jueves 10:00 a 12:00")).cupo(2)
                                .build();

    PeticionPorMateria peticionPorMateria = PeticionPorMateria.builder()
                                                              .comisiones(List.of(comision))
                                                              .build();

    return PeticionInscripcion.builder().estudiante(estudiante)
                              .peticionPorMaterias(List.of(peticionPorMateria)).build();
  }

  public static PeticionInscripcion crearPeticionInscripcionDummy() {
    Estudiante estudiante = crearEstudianteDummy("12345678");

    Materia materia = Materia.builder().codigo("MAT101").nombre("Matemática I").build();

    Comision comision = Comision.builder().codigo("MAT1-01-c3").materia(materia)
                                .horario(HorarioParser.parse("Martes 10:00 a 12:00")).cupo(30)
                                .build();

    PeticionPorMateria peticionPorMateria = PeticionPorMateria.builder()
                                                              .comisiones(List.of(comision))
                                                              .build();

    return PeticionInscripcion.builder().estudiante(estudiante)
                              .peticionPorMaterias(List.of(peticionPorMateria)).build();
  }


  public static ChatResponse respuestaChatResponsePriorizacion() {
    String json = """
        [
          {
            "a": "45072112",
            "ep": [
              { "n": "1041", "p": 91, "e": ["COR", "AVZ"] }
            ]
          }
        ]
      """;
    return new ChatResponse(List.of(new Generation(new AssistantMessage(json))));
  }

  public static ChatResponse respuestaChatResponseTraduccion() {
    String json = """
      [
        {
          "a": "45072112",
          "c": "1041-1-G14",
          "p": 90,
          "m": "Está por terminar la carrera, cumple correlativas y tiene un buen promedio.",
          "x": true
        }
      ]
      """;
    return new ChatResponse(List.of(new Generation(new AssistantMessage(json))));
  }

  public static ChatResponse respuestaChatResponseDummy() {
    return respuestaChatResponsePriorizacion();
  }
}