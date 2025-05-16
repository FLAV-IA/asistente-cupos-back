package com.edu.asistenteCupos.domain.peticion;

import com.edu.asistenteCupos.domain.asignacion.ResultadoAsignacion;
import com.edu.asistenteCupos.domain.sugerencia.SugerenciaInscripcion;
import com.edu.asistenteCupos.service.asignacion.AsignadorPorMateria;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Builder
@Data
public class PeticionPriorizada {
  private PeticionInscripcion inscripcion;
  private int prioridad;
  private String motivo;

  public Set<String> codigosComisionesSolicitadas() {
    return inscripcion.codigosDeComisionesSolicitadas();
  }

  public List<SugerenciaInscripcion> generarSugerencias(Map<String, Integer> cuposDisponibles, AsignadorPorMateria asignador) {
    return inscripcion.getPeticionPorMaterias().stream().map(peticion -> {
      ResultadoAsignacion resultado = asignador.asignar(peticion, cuposDisponibles);
      return resultado.crearSugerencia(inscripcion.getEstudiante(), peticion.getMateria(), motivo,
        prioridad);
    }).toList();
  }
}