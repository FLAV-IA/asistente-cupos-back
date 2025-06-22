package com.edu.asistente_cupos.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Asignacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAsignacion;

    @ManyToOne(optional = false)
    @JoinColumn(name = "dni_estudiante", referencedColumnName = "dni")
    private Estudiante estudiante;

    @ManyToOne(optional = false)
    @JoinColumn(name = "codigo_comision", referencedColumnName = "codigo")
    private Comision comision;

    @Column(name = "fecha_asignacion", nullable = false)
    private LocalDate fechaAsignacion;
}
