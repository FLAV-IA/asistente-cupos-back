package com.edu.asistente_cupos.controller.dto;

import java.util.List;

public record PeticionInscripcionDTO(String nombre, String dni,
                                     HistoriaAcademicaDTO historiaAcademica,
                                     List<PeticionPorMateriaDTO> materias) {}