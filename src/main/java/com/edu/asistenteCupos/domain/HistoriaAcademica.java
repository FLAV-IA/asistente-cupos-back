package com.edu.asistenteCupos.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class HistoriaAcademica {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long idHistoriaAcademica;

  @OneToOne
  @JoinColumn(name = "legajo_de_estudiante", referencedColumnName = "legajo", unique = true)
  @JsonBackReference
  private Estudiante estudiante;

  private int totalInscripcionesHistoricas;
  private Double coeficiente;
  private Boolean cumpleCorrelatividad;

  @OneToMany
  private List<Materia> ultimasCursadas;

  private String curso1c;
  private String aprob1c;
  private String curso2c;
  private String aprob2c;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
    name = "inscripciones_actuales",
    joinColumns = @JoinColumn(name = "id_historia_academica"),
    inverseJoinColumns = @JoinColumn(name = "codigo_de_materia"),
    uniqueConstraints = @UniqueConstraint(columnNames = {"id_historia_academica", "codigo_de_materia"})
  )
  private Set<Materia> inscripcionesActuales = new HashSet<>();
}