package com.edu.asistenteCupos.service.factory;

import com.edu.asistenteCupos.controller.dto.PeticionInscripcionCsvDTO;
import com.edu.asistenteCupos.domain.Comision;
import com.edu.asistenteCupos.domain.Estudiante;
import com.edu.asistenteCupos.domain.Materia;
import com.edu.asistenteCupos.domain.peticion.PeticionPorMateria;
import com.edu.asistenteCupos.excepcion.ComisionNoEncontradaException;
import com.edu.asistenteCupos.excepcion.ComisionesDeDistintaMateriaException;
import com.edu.asistenteCupos.excepcion.NoSeEspecificaronComisionesException;
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

    if (comisiones.contains(null)) {
      throw new ComisionNoEncontradaException(
        "No se encontraron todas las comisiones especificadas.");
    }

    Materia materiaReferencia = comisiones.get(0).getMateria();
    boolean todasMismaMateria = comisiones.stream()
                                          .allMatch(c -> c.getMateria().equals(materiaReferencia));
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
