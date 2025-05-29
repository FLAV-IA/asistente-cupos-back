package com.edu.asistente_cupos.filtros;

import com.edu.asistente_cupos.domain.Comision;
import com.edu.asistente_cupos.domain.Estudiante;
import com.edu.asistente_cupos.domain.filtros.FiltroAComisionesSinCupo;
import com.edu.asistente_cupos.domain.peticion.PeticionInscripcion;
import com.edu.asistente_cupos.domain.peticion.PeticionPorMateria;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FiltroComisionesSinCupoTest {


  private final FiltroAComisionesSinCupo filtro = new FiltroAComisionesSinCupo();

  @Test
  @DisplayName("Debe filtrar comisiones sin cupo en una peticion de inscripcion")
  void testFiltrarComisionesSinCupo() {
    Estudiante estudiante = crearEstudiante("40000001", "Carla");

    Comision sinCupo = crearComision("C1", 0);
    Comision conCupo = crearComision("C2", 5);
    PeticionPorMateria ppm = crearPeticionPorMateria(sinCupo, conCupo);
    PeticionInscripcion peticion = crearPeticion(estudiante, ppm);

    List<PeticionInscripcion> resultado = filtro.filtrar(List.of(peticion));

    assertEquals(1, resultado.size());
    List<Comision> comisionesRestantes = resultado.get(0).getPeticionPorMaterias().get(0)
                                                  .getComisiones();
    assertEquals(1, comisionesRestantes.size());
    assertEquals("C2", comisionesRestantes.get(0).getCodigo());
  }

  @Test
  @DisplayName("Debe eliminar PeticionPorMateria si todas las comisiones no tienen cupo")
  void testEliminarPeticionPorMateriaSinCupo() {
    Estudiante estudiante = crearEstudiante("40000002", "Lucas");

    Comision sinCupo1 = crearComision("A1", 0);
    Comision sinCupo2 = crearComision("A2", 0);
    PeticionPorMateria ppm = crearPeticionPorMateria(sinCupo1, sinCupo2);
    PeticionInscripcion peticion = crearPeticion(estudiante, ppm);

    List<PeticionInscripcion> resultado = filtro.filtrar(List.of(peticion));

    assertEquals(0, resultado.size());
  }

  @Test
  @DisplayName("Debe omitir solo comisiones sin cupo dentro de una PeticionPorMateria")
  void omitirSoloComisionesSinCupoDeUnaPeticionPorMateria() {
    Estudiante estudiante = crearEstudiante("40000003", "Julian");

    Comision sinCupo = crearComision("D1", 0);
    Comision conCupo = crearComision("D2", 7);
    PeticionPorMateria ppm = crearPeticionPorMateria(sinCupo, conCupo);
    PeticionInscripcion peticion = crearPeticion(estudiante, ppm);

    List<PeticionInscripcion> resultado = filtro.filtrar(List.of(peticion));

    assertEquals(1, resultado.size());
    List<Comision> comisionesRestantes = resultado.get(0).getPeticionPorMaterias().get(0)
                                                  .getComisiones();
    assertEquals(1, comisionesRestantes.size());
    assertEquals("D2", comisionesRestantes.get(0).getCodigo());
  }


  private Estudiante crearEstudiante(String dni, String nombre) {
    return Estudiante.builder().dni(dni).nombre(nombre).build();
  }

  private Comision crearComision(String codigo, int cupo) {
    return Comision.builder().codigo(codigo).cupo(cupo).build();
  }

  private PeticionPorMateria crearPeticionPorMateria(Comision... comisiones) {
    PeticionPorMateria ppm = PeticionPorMateria.builder().build();
    ppm.setComisiones(List.of(comisiones));
    return ppm;
  }

  private PeticionInscripcion crearPeticion(Estudiante estudiante, PeticionPorMateria... ppm) {
    return PeticionInscripcion.builder().estudiante(estudiante).peticionPorMaterias(List.of(ppm))
                              .build();
  }
}
