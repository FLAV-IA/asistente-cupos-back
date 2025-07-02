package com.edu.asistente_cupos.controller;

import com.edu.asistente_cupos.controller.dto.InfoComisionDto;
import com.edu.asistente_cupos.service.AsistenteDeComisiones;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/comisiones")
@RequiredArgsConstructor
@Slf4j
public class ComisionesController {
    private final AsistenteDeComisiones asistenteComisiones;

    @GetMapping("/obtenerComisiones")
    public List<InfoComisionDto> obtenerComisiones() {
        return this.asistenteComisiones.obtenerComisiones();
    }

}
