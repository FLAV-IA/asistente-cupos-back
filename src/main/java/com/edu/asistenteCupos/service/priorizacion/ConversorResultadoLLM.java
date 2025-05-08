package com.edu.asistenteCupos.service.priorizacion;

import com.edu.asistenteCupos.domain.PeticionInscripcion;
import com.edu.asistenteCupos.domain.PeticionPriorizada;
import com.edu.asistenteCupos.service.priorizacion.dto.ResultadoPriorizacionLLM;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ConversorResultadoLLM {
  public List<PeticionPriorizada> desdeResultadosLLM(List<ResultadoPriorizacionLLM> resultadosLLM, List<PeticionInscripcion> originales) {
    Map<String, PeticionInscripcion> inscripcionPorDni = originales.stream().collect(
      Collectors.toMap(p -> p.getEstudiante().getDni(), p -> p));

    List<PeticionPriorizada> resultado = new ArrayList<>();

    for (ResultadoPriorizacionLLM resultadoLLM : resultadosLLM) {
      String dni = resultadoLLM.getA();

      PeticionInscripcion inscripcion = inscripcionPorDni.get(dni);
      if (inscripcion == null) {
        throw new IllegalStateException("No se encontró inscripción para estudiante: " + dni);
      }

      PeticionPriorizada priorizada = PeticionPriorizada.builder().inscripcion(inscripcion)
                                                        .prioridad(resultadoLLM.getP()).motivo(
          String.join(",", resultadoLLM.getE())).build();

      resultado.add(priorizada);
    }

    return resultado;
  }
}
