package com.edu.asistente_cupos.service.traduccion;

import com.edu.asistente_cupos.domain.Comision;
import com.edu.asistente_cupos.domain.Estudiante;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaAceptada;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaInscripcion;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaRechazada;
import com.edu.asistente_cupos.repository.ComisionRepository;
import com.edu.asistente_cupos.repository.EstudianteRepository;
import com.edu.asistente_cupos.service.traduccion.dto.SugerenciaInscripcionLLM;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ConversorSugerenciasLLM {
  private final EstudianteRepository repoEstudiantes;
  private final ComisionRepository repoComisiones;

  public SugerenciaInscripcion convertir(SugerenciaInscripcionLLM sugerenciaInscripcionLLM) {
    Estudiante estudiante = repoEstudiantes.findByDni(sugerenciaInscripcionLLM.getA()).orElseThrow(
      () -> new RuntimeException("Estudiante no encontrado: " + sugerenciaInscripcionLLM.getA()));
    Comision comision = repoComisiones.findById(sugerenciaInscripcionLLM.getC()).orElseThrow(
      () -> new RuntimeException("Comisión no encontrada: " + sugerenciaInscripcionLLM.getC()));
    if (sugerenciaInscripcionLLM.isX()) {
      return new SugerenciaAceptada(estudiante, comision.getMateria(), comision,
        sugerenciaInscripcionLLM.getM(), sugerenciaInscripcionLLM.getP());
    } else {
      return new SugerenciaRechazada(estudiante, comision.getMateria(), comision,
        sugerenciaInscripcionLLM.getM(), sugerenciaInscripcionLLM.getP());
    }
  }

  public List<SugerenciaInscripcion> desdeLLM(List<SugerenciaInscripcionLLM> lista) {
    return lista.stream().map(this::convertir).toList();
  }
}
