package com.edu.asistenteCupos.pipeline;

import com.edu.asistenteCupos.domain.filtros.FiltroDePeticionInscripcion;
import com.edu.asistenteCupos.domain.peticion.PeticionInscripcion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PasoFiltradoDePeticiones implements Paso<List<PeticionInscripcion>, List<PeticionInscripcion>> {
  private final FiltroDePeticionInscripcion filtro;

  @Override
  public List<PeticionInscripcion> ejecutar(List<PeticionInscripcion> input) {
    return filtro.filtrar(input);
  }
}
