package com.edu.asistente_cupos.service;

import com.edu.asistente_cupos.assembler.EnsambladorDeSugerenciasAceptadas;
import com.edu.asistente_cupos.domain.Asignacion;
import com.edu.asistente_cupos.domain.Comision;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaInscripcion;
import com.edu.asistente_cupos.repository.ComisionRepository;
import com.edu.asistente_cupos.service.asignacion.AsignadorDeSugerencias;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AsistenteDeAsignacion {
    private final AsignadorDeSugerencias asignadorDeSugerencias;
    private final ComisionRepository comisionRepository;
    private final Set<Comision> comisionesModificadas = new HashSet<>();
    private final EnsambladorDeSugerenciasAceptadas ensambladorDeSugerenciasAceptadas;
    /**
     * Asigna sugerencias aceptadas a las comisiones correspondientes.
     * Limpia las comisiones modificadas antes de asignar nuevas sugerencias.
     *
     * @param sugerenciaInscripcionList Lista de sugerencias de inscripcion a asignar.
     */
    public void asignarSugerencias(List<SugerenciaInscripcion> sugerenciaInscripcionList) {
        limpiarComisionesModificadas();
        sugerenciaInscripcionList.forEach(this::asignarSugerencia);
    }

    private void asignarSugerencia(SugerenciaInscripcion sugerencia) {
        Comision comision = obtenerComision(sugerencia);
        if (comision.tieneCupo()) {
            asignadorDeSugerencias.asignarSugerencia(sugerencia);
            //agregar cursada al estudiante
            comisionesModificadas.add(comision);
        }
    }

    private Comision obtenerComision(SugerenciaInscripcion sugerencia) {
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

    public List<SugerenciaInscripcion> obtenerSugerenciasAsignadas() {
        List<Asignacion> asignaciones = this.asignadorDeSugerencias.obtenerAsignacionesParciales();
        return this.ensambladorDeSugerenciasAceptadas.ensamblarSugerenciasDesdeAsignaciones(asignaciones);
    }

    public void eliminarAsignacion(String codComision, String dniEstudiante) {
        Comision comision = comisionRepository.findById(codComision)
                .orElseThrow(() -> new IllegalArgumentException("Comision no encontrada: " + codComision));
        asignadorDeSugerencias.eliminarAsignacion(comision, dniEstudiante);
        comisionesModificadas.add(comision);
    }
}
