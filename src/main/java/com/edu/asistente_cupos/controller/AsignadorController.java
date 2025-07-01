package com.edu.asistente_cupos.controller;

import com.edu.asistente_cupos.assembler.EnsambladorDeSugerenciasAceptadas;
import com.edu.asistente_cupos.controller.dto.SugerenciaInscripcionDTO;
import com.edu.asistente_cupos.domain.Comision;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaInscripcion;
import com.edu.asistente_cupos.mapper.ComisionMapper;
import com.edu.asistente_cupos.service.AsistenteDeAsignacion;
import com.edu.asistente_cupos.service.sugerencia.opta.model.ComisionDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/asignador")
@RequiredArgsConstructor
@Slf4j
public class AsignadorController {
    private final AsistenteDeAsignacion asistenteDeAsignacion;
    private final EnsambladorDeSugerenciasAceptadas ensambladorDeSugerenciasAceptadas;
    private final ComisionMapper comisionMapper;

    @PostMapping("/asignar-sugerencias")
    public ResponseEntity<Set<ComisionDTO>> confirmarAsignaciones(
            @RequestBody List<SugerenciaInscripcionDTO> sugerenciasInscripcionAsignableDto) {
        try {
            List<SugerenciaInscripcion> sugerenciaInscripcionList = ensambladorDeSugerenciasAceptadas.ensamblarSugerencias(sugerenciasInscripcionAsignableDto);
            asistenteDeAsignacion.asignarSugerencias(sugerenciaInscripcionList);
            Set<Comision> comisionesModificadas = asistenteDeAsignacion.obtenerComisionesModificadas();
            Set<ComisionDTO> comisionDtoList = comisionMapper.toDtoList(comisionesModificadas);
            return ResponseEntity.ok(comisionDtoList);

        } catch (Exception e) {
            log.error("Error procesando archivo de asignaciones", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Set.of());
        }
    }

    @PostMapping("/eliminar-asignacion")
    public ResponseEntity<Void> eliminarAsignacion(@RequestParam String codComision,@RequestParam String dniEstudiante){
        try {
            asistenteDeAsignacion.eliminarAsignacion(codComision, dniEstudiante);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error eliminando asignacion", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
