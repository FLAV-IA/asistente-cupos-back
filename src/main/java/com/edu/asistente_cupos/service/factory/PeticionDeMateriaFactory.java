package com.edu.asistente_cupos.service.factory;

import com.edu.asistente_cupos.controller.dto.PeticionInscripcionCsvDTO;
import com.edu.asistente_cupos.domain.Comision;
import com.edu.asistente_cupos.domain.Estudiante;
import com.edu.asistente_cupos.domain.Materia;
import com.edu.asistente_cupos.domain.peticion.PeticionPorMateria;
import com.edu.asistente_cupos.excepcion.ComisionesDeDistintaMateriaException;
import com.edu.asistente_cupos.excepcion.NoSeEspecificaronComisionesException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class PeticionDeMateriaFactory {
  public PeticionPorMateria crearPeticionDeMateria(PeticionInscripcionCsvDTO dto, Map<String, Comision> comisionesPorCodigo, Estudiante estudiante) {
    String codigosRaw = dto.getCodigosComisiones();
    if (codigosRaw == null || codigosRaw.isBlank()) {
      throw new NoSeEspecificaronComisionesException(
        "No se especificaron códigos de comisión para la petición.");
    }

    List<String> codigoComisiones = Arrays.stream(codigosRaw.split(",")).map(String::trim)
                                          .filter(s -> !s.isBlank()).toList();

    if (codigoComisiones.isEmpty()) {
      throw new NoSeEspecificaronComisionesException(
        "No se especificaron códigos de comisión para la petición.");
    }

    List<Comision> comisiones = codigoComisiones.stream().map(comisionesPorCodigo::get).toList();


    Materia materiaReferencia = comisiones.get(0).getMateria();
    boolean todasMismaMateria = comisiones.stream().allMatch(
      c -> c != null && c.getMateria().equals(materiaReferencia));
    if (!todasMismaMateria) {
      throw new ComisionesDeDistintaMateriaException(
        "Las comisiones especificadas deben pertenecer a la misma materia: " +
          materiaReferencia.getNombre() + " (" + materiaReferencia.getCodigo() + ").");
    }

    boolean cumpleCorrelativa = estudiante.puedeInscribirse(materiaReferencia);

    return PeticionPorMateria.builder().comisiones(comisiones).cumpleCorrelativa(cumpleCorrelativa)
                             .build();
  }
}
