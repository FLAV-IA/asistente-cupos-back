package com.edu.asistente_cupos.service;

import com.edu.asistente_cupos.domain.Comision;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaAceptada;
import com.edu.asistente_cupos.repository.ComisionRepository;
import com.edu.asistente_cupos.service.asignacion.AsignadorDeSugerencias;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class AsistenteDeAsignacion {
    private final AsignadorDeSugerencias asignadorDeSugerencias;
    private final ComisionRepository comisionRepository;
    private final Set<Comision> comisionesModificadas = new HashSet<>();

    public void asignarSugerencias(List<SugerenciaAceptada> sugerenciasAceptada) {
        limpiarComisionesModificadas();
        sugerenciasAceptada.forEach(this::asignarSugerencia);
    }

    private void asignarSugerencia(SugerenciaAceptada sugerencia) {
        Comision comision = obtenerComision(sugerencia);
        if (comision.tieneCupo()) {
            asignadorDeSugerencias.asignarSugerencia(sugerencia);
            //agregar cursada al estudiante
            comisionesModificadas.add(comision);
        }
    }

    private Comision obtenerComision(SugerenciaAceptada sugerencia) {
        return comisionRepository.findById(sugerencia.comision().getCodigo())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Comision no encontrada: " + sugerencia.comision().getCodigo()));
    }

    public Set<Comision> obtenerComisionesModificadas() {
        return new HashSet<>(comisionesModificadas);
    }

    public void limpiarComisionesModificadas() {
        comisionesModificadas.clear();
    }
}
