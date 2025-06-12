package com.edu.asistente_cupos.domain.filtros;

import com.edu.asistente_cupos.domain.peticion.PeticionInscripcion;
import com.edu.asistente_cupos.repository.AsignacionRepository;

import java.util.List;

public class FiltroDePeticionYaAsignada   extends FiltroDePeticionInscripcion{
    private final AsignacionRepository asignacionRepository;
    public FiltroDePeticionYaAsignada(AsignacionRepository asignacionRepository) {
        this.asignacionRepository =asignacionRepository;
    }

    @Override
    protected List<PeticionInscripcion> aplicarFiltro(List<PeticionInscripcion> peticiones) {
        return filtrarPeticionesPorComision(peticiones,
                peticion -> comision ->
                    asignacionRepository
                        .existsByEstudianteAndComision(
                                peticion.getEstudiante(),
                                comision
                        )

                );    }
}
