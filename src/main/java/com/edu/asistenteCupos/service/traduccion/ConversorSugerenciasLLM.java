package com.edu.asistenteCupos.service.traduccion;

import com.edu.asistenteCupos.domain.Comision;
import com.edu.asistenteCupos.domain.Estudiante;
import com.edu.asistenteCupos.domain.sugerencia.SugerenciaAsignada;
import com.edu.asistenteCupos.domain.sugerencia.SugerenciaInscripcion;
import com.edu.asistenteCupos.domain.sugerencia.SugerenciaRechazada;
import com.edu.asistenteCupos.repository.ComisionRepository;
import com.edu.asistenteCupos.repository.EstudianteRepository;
import com.edu.asistenteCupos.service.traduccion.dto.SugerenciaInscripcionLLM;
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
      return new SugerenciaAsignada(estudiante, comision.getMateria(), comision,
        sugerenciaInscripcionLLM.getM(), sugerenciaInscripcionLLM.getP());
    } else {
      return new SugerenciaRechazada(estudiante, comision.getMateria(),
        sugerenciaInscripcionLLM.getM(), sugerenciaInscripcionLLM.getP());
    }
  }

  public List<SugerenciaInscripcion> desdeLLM(List<SugerenciaInscripcionLLM> lista) {
    return lista.stream().map(this::convertir).toList();
  }
}
