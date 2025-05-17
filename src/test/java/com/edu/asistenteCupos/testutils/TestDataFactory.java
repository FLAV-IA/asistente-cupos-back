package com.edu.asistenteCupos.testutils;

import com.edu.asistenteCupos.domain.Comision;
import com.edu.asistenteCupos.domain.Estudiante;
import com.edu.asistenteCupos.domain.Materia;
import com.edu.asistenteCupos.domain.peticion.PeticionInscripcion;
import com.edu.asistenteCupos.domain.peticion.PeticionPorMateria;
import com.edu.asistenteCupos.domain.priorizacion.PeticionPorMateriaPriorizada;
import com.edu.asistenteCupos.domain.sugerencia.SugerenciaAsignada;
import com.edu.asistenteCupos.domain.sugerencia.SugerenciaInscripcion;
import com.edu.asistenteCupos.domain.sugerencia.SugerenciaRechazada;
import com.edu.asistenteCupos.service.priorizacion.dto.ResultadoPriorizacionLLM;
import com.edu.asistenteCupos.service.traduccion.dto.SugerenciaInscripcionLLM;

import java.util.List;

public class TestDataFactory {
  public static PeticionInscripcion crearPeticionInscripcionDummy() {
    return PeticionInscripcion.builder().estudiante(crearEstudianteDummy())
                              .peticionPorMaterias(List.of(crearPeticionPorMateriaDummy())).build();
  }

  public static Estudiante crearEstudianteDummy() {
    return crearEstudianteDummy("12345678");
  }

  public static Estudiante crearEstudianteDummy(String dni) {
    return Estudiante.builder().dni(dni).legajo("LEG" + dni).nombre("Estudiante " + dni)
                     .mail(dni + "@test.com").build();
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
    return new Comision(codigo, "Lunes 10-12", cupo, materia);
  }

  public static PeticionPorMateria crearPeticionPorMateriaDummy() {
    return PeticionPorMateria.builder().comisiones(List.of(crearComisionDummy()))
                             .cumpleCorrelativa(true).build();
  }

  public static PeticionPorMateriaPriorizada crearPeticionPriorizadaDummy() {
    return PeticionPorMateriaPriorizada.builder().estudiante(crearEstudianteDummy())
                                       .materia(crearMateriaDummy())
                                       .comisionesSolicitadas(List.of(crearComisionDummy()))
                                       .cumpleCorrelativa(true).prioridad(1).motivo("COR").build();
  }

  public static ResultadoPriorizacionLLM crearResultadoPriorizacionLLMDummy() {
    ResultadoPriorizacionLLM.EvaluacionPrioridad evaluacion = new ResultadoPriorizacionLLM.EvaluacionPrioridad();
    evaluacion.setN("MAT1");
    evaluacion.setP(91);
    evaluacion.setE(List.of("COR", "AVZ"));

    return new ResultadoPriorizacionLLM("12345678", List.of(evaluacion));
  }

  public static SugerenciaInscripcion crearSugerenciaAsignadaDummy() {
    return new SugerenciaAsignada(crearEstudianteDummy(), crearMateriaDummy(), crearComisionDummy(),
      "Asignada [COR]", 1);
  }

  public static SugerenciaInscripcion crearSugerenciaRechazadaDummy() {
    return new SugerenciaRechazada(crearEstudianteDummy(), crearMateriaDummy(),
      "Rechazada por falta de cupo", 2);
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
}