package com.edu.asistenteCupos.domain.filtros;

import com.edu.asistenteCupos.domain.peticion.PeticionInscripcion;

import java.util.List;

public interface FiltroDePeticionInscripcion {
  void setFiltroSiguiente(FiltroDePeticionInscripcion siguiente);

  List<PeticionInscripcion> filtrar(List<PeticionInscripcion> peticiones);
}