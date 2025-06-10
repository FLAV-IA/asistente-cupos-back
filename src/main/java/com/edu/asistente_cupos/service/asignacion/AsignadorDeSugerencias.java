package com.edu.asistente_cupos.service.asignacion;

import com.edu.asistente_cupos.domain.Asignacion;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaAceptada;
import com.edu.asistente_cupos.repository.AsignacionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class AsignadorDeSugerencias {
    private final AsignacionRepository asignacionRepository;


    public Asignacion asignarSugerencia(SugerenciaAceptada sugerenciaRecomendada) {
        Asignacion asignacion = Asignacion.builder()
                .estudiante(sugerenciaRecomendada.estudiante())//
                .comision(sugerenciaRecomendada.comision())
                .fechaAsignacion(LocalDate.now())
                .build();
        return this.asignacionRepository.save(asignacion);
    }
}
