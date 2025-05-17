package com.edu.asistenteCupos.service.priorizacion;

import com.edu.asistenteCupos.domain.Comision;
import com.edu.asistenteCupos.domain.Estudiante;
import com.edu.asistenteCupos.domain.Materia;
import com.edu.asistenteCupos.domain.peticion.PeticionInscripcion;
import com.edu.asistenteCupos.domain.peticion.PeticionPorMateria;
import com.edu.asistenteCupos.domain.priorizacion.PeticionPorMateriaPriorizada;
import com.edu.asistenteCupos.service.priorizacion.dto.ResultadoPriorizacionLLM;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ConversorResultadoLLM {
  public List<PeticionPorMateriaPriorizada> desdeResultadosLLM(List<ResultadoPriorizacionLLM> resultadosLLM, List<PeticionInscripcion> originales) {
    List<PeticionPorMateriaPriorizada> peticionesPriorizadas = new ArrayList<>();

    for (ResultadoPriorizacionLLM resultado : resultadosLLM) {
      PeticionInscripcion inscripcion = buscarInscripcionPorDNI(resultado.getA(), originales);
      peticionesPriorizadas.addAll(mapear(resultado, inscripcion));
    }

    return peticionesPriorizadas;
  }

  private List<PeticionPorMateriaPriorizada> mapear(ResultadoPriorizacionLLM resultado, PeticionInscripcion inscripcion) {
    Estudiante estudiante = inscripcion.getEstudiante();

    Map<String, PeticionPorMateria> peticionesPorCodigo = inscripcion.getPeticionPorMaterias()
                                                                     .stream().collect(
        Collectors.toMap(PeticionPorMateria::getCodigoMateria, Function.identity()));

    List<PeticionPorMateriaPriorizada> resultadoFinal = new ArrayList<>();

    for (ResultadoPriorizacionLLM.EvaluacionPrioridad ep : resultado.getEp()) {
      PeticionPorMateria peticionOriginal = peticionesPorCodigo.get(ep.getN());

      if (peticionOriginal == null) {
        throw new IllegalArgumentException(
          "No se encontró una petición original para la materia " + ep.getN());
      }

      Materia materia = peticionOriginal.getMateria();
      List<Comision> comisiones = peticionOriginal.getComisiones();
      boolean cumpleCorrelativa = peticionOriginal.isCumpleCorrelativa();
      String motivo = String.join(",", ep.getE());

      resultadoFinal.add(
        new PeticionPorMateriaPriorizada(estudiante, materia, comisiones, cumpleCorrelativa,
          ep.getP(), motivo));
    }

    return resultadoFinal;
  }

  private PeticionInscripcion buscarInscripcionPorDNI(String dni, List<PeticionInscripcion> lista) {
    return lista.stream().filter(p -> p.getEstudiante().getDni().equals(dni)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                  "No se encontró la inscripción del estudiante con DNI " + dni));
  }
}
