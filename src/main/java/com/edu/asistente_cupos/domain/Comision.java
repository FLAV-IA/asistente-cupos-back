package com.edu.asistente_cupos.domain;

import com.edu.asistente_cupos.domain.horario.Horario;
import com.edu.asistente_cupos.mapper.HorarioConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comision {
    @Id
    private String codigo;

    @Convert(converter = HorarioConverter.class)
    private Horario horario;

    private int cupo;

    @ManyToOne
    @JoinColumn(name = "codigo-materia", referencedColumnName = "codigo")
    private Materia materia;

    @OneToMany(mappedBy = "comision", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Asignacion> asignaciones = new ArrayList<>();

    public boolean tieneCupo() {
        return this.cupo - estudiantesInscriptos().size() > 0;
    }

    public List<Estudiante> estudiantesInscriptos() {
        return asignaciones.stream()
                .map(Asignacion::getEstudiante)
                .toList();
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Comision that = (Comision) o;
        return Objects.equals(codigo, that.codigo);
    }
}
