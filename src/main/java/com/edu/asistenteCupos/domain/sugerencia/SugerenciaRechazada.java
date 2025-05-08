package com.edu.asistenteCupos.domain.sugerencia;

import com.edu.asistenteCupos.domain.Estudiante;
import com.edu.asistenteCupos.domain.Materia;

public record SugerenciaRechazada(Estudiante estudiante, Materia materia, String motivo,
                                  int prioridad) implements SugerenciaInscripcion {}