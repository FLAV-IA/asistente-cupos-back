package com.edu.asistente_cupos.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InfoComisionDto {
    private String codigo;
    private String materia;
    private String horario;
    private int cantidadInscriptos;
    private int cuposTotales;
    private List<EstudianteDto> estudiantesInscriptos;

}
