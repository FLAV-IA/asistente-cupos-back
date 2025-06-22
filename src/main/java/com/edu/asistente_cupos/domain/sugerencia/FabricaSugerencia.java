package com.edu.asistente_cupos.domain.sugerencia;

import com.edu.asistente_cupos.domain.Comision;
import com.edu.asistente_cupos.domain.Estudiante;
import com.edu.asistente_cupos.domain.Materia;

@FunctionalInterface
public interface FabricaSugerencia {
    SugerenciaInscripcion crear(Estudiante estudiante, Materia materia, Comision comision, String motivo, int prioridad);
}