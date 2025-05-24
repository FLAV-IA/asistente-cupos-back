package com.edu.asistenteCupos.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HistoriaAcademicaDTO {
        private String dni;
        private int totalInscripcionesHistoricas;
        private int totalHistoricasAprobadas;
        private double coeficiente;
        private boolean cumpleCorrelativas;
        private List<String> codigosCursadasAnteriores;
        private Set<String> codigosInscripcionesActuales;
    }