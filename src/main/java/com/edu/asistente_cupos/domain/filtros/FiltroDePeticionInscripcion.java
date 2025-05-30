package com.edu.asistente_cupos.domain.filtros;

import com.edu.asistente_cupos.domain.Comision;
import com.edu.asistente_cupos.domain.peticion.PeticionInscripcion;
import com.edu.asistente_cupos.domain.peticion.PeticionPorMateria;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class FiltroDePeticionInscripcion {
  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  private Optional<FiltroDePeticionInscripcion> siguiente = Optional.empty();

  public void setFiltroSiguiente(FiltroDePeticionInscripcion siguiente) {
    this.siguiente = Optional.ofNullable(siguiente);
  }

  public List<PeticionInscripcion> filtrar(List<PeticionInscripcion> peticiones) {
    List<PeticionInscripcion> filtradas = aplicarFiltro(peticiones);
    return siguiente.map(f -> f.filtrar(filtradas)).orElse(filtradas);
  }

  protected abstract List<PeticionInscripcion> aplicarFiltro(List<PeticionInscripcion> peticiones);

  protected List<PeticionInscripcion> filtrarPeticionesPorComision(List<PeticionInscripcion> peticiones, Function<PeticionInscripcion, Predicate<Comision>> criterioPorPeticion) {
    return peticiones.stream().map(p -> filtrarPeticion(p, criterioPorPeticion.apply(p)))
                     .filter(Objects::nonNull).toList();
  }

  private PeticionInscripcion filtrarPeticion(PeticionInscripcion peticion, Predicate<Comision> criterio) {
    List<PeticionPorMateria> materiasFiltradas = peticion.getPeticionPorMaterias().stream().map(
      ppm -> filtrarPorMateria(ppm, criterio)).filter(Objects::nonNull).toList();

    if (materiasFiltradas.isEmpty())
      return null;

    return PeticionInscripcion.builder().estudiante(peticion.getEstudiante())
                              .peticionPorMaterias(materiasFiltradas).build();
  }

  private PeticionPorMateria filtrarPorMateria(PeticionPorMateria ppm, Predicate<Comision> criterio) {
    List<Comision> comisionesFiltradas = ppm.getComisiones().stream().filter(criterio).toList();

    if (comisionesFiltradas.isEmpty())
      return null;

    return PeticionPorMateria.builder().comisiones(comisionesFiltradas)
                             .cumpleCorrelativa(ppm.isCumpleCorrelativa()).build();
  }

  protected List<PeticionInscripcion> filtrarPeticiones(List<PeticionInscripcion> peticiones, Predicate<PeticionInscripcion> predicate) {
    return peticiones.stream().filter(predicate).toList();
  }
}