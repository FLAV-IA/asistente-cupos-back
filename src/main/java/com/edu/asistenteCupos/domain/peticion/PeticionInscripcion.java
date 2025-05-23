package com.edu.asistenteCupos.domain.peticion;

import com.edu.asistenteCupos.domain.Estudiante;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class PeticionInscripcion {
  private Estudiante estudiante;
  private List<PeticionPorMateria> peticionPorMaterias;
}
