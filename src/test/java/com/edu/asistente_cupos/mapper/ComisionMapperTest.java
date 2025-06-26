package com.edu.asistente_cupos.mapper;

import com.edu.asistente_cupos.controller.dto.InfoComisionDto;
import com.edu.asistente_cupos.domain.Asignacion;
import com.edu.asistente_cupos.domain.Comision;
import com.edu.asistente_cupos.domain.Estudiante;
import com.edu.asistente_cupos.domain.Materia;
import com.edu.asistente_cupos.domain.horario.Horario;
import com.edu.asistente_cupos.service.sugerencia.opta.model.ComisionDTO;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ComisionMapperTest {

    private final ComisionMapper mapper = Mappers.getMapper(ComisionMapper.class);

    @Test
    void testToDto() {
        Comision comision = crearComision();
        ComisionDTO dto = mapper.toDto(comision);

        assertEquals("MAT123", dto.getMateriaCodigo());
    }

    @Test
    void testToComisionActualizadaDto() {
        Comision comision = crearComision();
        InfoComisionDto dto = mapper.toComisionActualizadaDto(comision);

        assertEquals("COM001", dto.getCodigo());
        assertEquals("Matematica", dto.getMateria());
        assertEquals(2, dto.getCantidadInscriptos());
        assertEquals(30, dto.getCuposTotales());
    }

    @Test
    void testToDtoList() {
        List<ComisionDTO> dtos = mapper.toDtoList(List.of(crearComision()));
        assertEquals(1, dtos.size());
    }

    @Test
    void testToDtoSet() {
        Set<ComisionDTO> dtos = mapper.toDtoList(Set.of(crearComision()));
        assertEquals(1, dtos.size());
    }

    @Test
    void testToComisionesActualizadasDtoList() {
        List<InfoComisionDto> dtos = mapper.toComisionesActualizadasDtoList(List.of(crearComision()));
        assertEquals(1, dtos.size());
    }



    private Comision crearComision() {
        Materia materia = Materia.builder()
                .codigo("MAT123")
                .nombre("Matematica")
                .build();

        Estudiante e1 = Estudiante.builder().dni("12345678").build();
        Estudiante e2 = Estudiante.builder().dni("87654321").build();

        Comision comision = Comision.builder()
                .codigo("COM001")
                .horario(new Horario(List.of()))
                .cupo(30)
                .materia(materia)
                .asignaciones(List.of())
                .build();

        comision.setAsignaciones(List.of(
                this.asignacion(comision, e1),
                this.asignacion(comision, e2)
        ));

        return comision;
    }

    public static Asignacion asignacion(Comision comision, Estudiante estudiante) {
        Asignacion a = new Asignacion();
        a.setComision(comision);
        a.setEstudiante(estudiante);
        return a;
    }
}
