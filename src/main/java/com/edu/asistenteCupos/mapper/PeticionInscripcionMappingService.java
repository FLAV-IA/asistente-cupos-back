package com.edu.asistenteCupos.mapper;

import com.edu.asistenteCupos.domain.Comision;
import com.edu.asistenteCupos.domain.Estudiante;
import com.edu.asistenteCupos.domain.Materia;
import com.edu.asistenteCupos.repository.ComisionRepository;
import com.edu.asistenteCupos.repository.EstudianteRepository;
import com.edu.asistenteCupos.repository.MateriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PeticionInscripcionMappingService {
  private final EstudianteRepository estudianteRepository;
  private final ComisionRepository comisionRepository;
  private final MateriaRepository materiaRepository;

  public Estudiante buscarEstudiantePorDni(String dni) {
    return estudianteRepository.findByCodigo(dni).orElseThrow(
      () -> new IllegalArgumentException("No se encontró Estudiante con dni: " + dni));
  }

  public Comision buscarComisionPorCodigo(String codigo) {
    return comisionRepository.findById(codigo).orElseThrow(
      () -> new IllegalArgumentException("No se encontró Comisión con código: " + codigo));
  }

  public Materia buscarMateriaPorCodigo(String codigo) {
    return materiaRepository.findByCodigo(codigo).orElseThrow(
      () -> new IllegalArgumentException("No se encontró Materia con código: " + codigo));
  }
}
