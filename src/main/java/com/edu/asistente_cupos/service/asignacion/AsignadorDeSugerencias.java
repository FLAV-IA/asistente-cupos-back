package com.edu.asistente_cupos.service.asignacion;

import com.edu.asistente_cupos.domain.Asignacion;
import com.edu.asistente_cupos.domain.Comision;
import com.edu.asistente_cupos.domain.Estudiante;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaAceptada;
import com.edu.asistente_cupos.repository.AsignacionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AsignadorDeSugerencias {
    private final AsignacionRepository asignacionRepository;


    public Asignacion asignarSugerencia(SugerenciaAceptada sugerenciaRecomendada) {
        Estudiante estudiante = sugerenciaRecomendada.estudiante();
        Comision comision = sugerenciaRecomendada.comision();
        Optional<Asignacion> byEstudianteAndComision = this.asignacionRepository.findByEstudianteAndComision(estudiante, comision);

        if(byEstudianteAndComision.isPresent()) {
            log.info("La asignación ya existe para el estudiante {} y la comisión {}", estudiante.getDni(), comision.getCodigo());
            return byEstudianteAndComision.get();
        }

        Asignacion asignacion = Asignacion.builder()
                .estudiante(estudiante)//
                .comision(comision)
                .fechaAsignacion(LocalDate.now())
                .build();
        return this.asignacionRepository.save(asignacion);
    }

    public List<Asignacion> obtenerAsignacionesParciales() {
       return this.asignacionRepository.findAll();
    }
}
