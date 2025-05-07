package com.edu.asistenteCupos.domain.filtros;

import com.edu.asistenteCupos.domain.PeticionInscripcion;

import java.util.List;

public interface FiltroDePeticionInscripcion {
  void setFiltroSiguiente(FiltroDePeticionInscripcion siguiente);

  List<PeticionInscripcion> filtrar(List<PeticionInscripcion> peticiones);
}