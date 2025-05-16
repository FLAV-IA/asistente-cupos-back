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
  private int totalInscripcionesHistoricas;
  private int totalHistoricasAprobadas;
  private Double coeficiente;

  @OneToOne
  @JoinColumn(name = "dni_de_estudiante", referencedColumnName = "dni", unique = true)
  @JsonBackReference
  private Estudiante estudiante;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Cursada> cursadasAnteriores;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "inscripciones_actuales",
    joinColumns = @JoinColumn(name = "id_historia_academica"),
    inverseJoinColumns = @JoinColumn(name = "codigo_de_materia"),
    uniqueConstraints = @UniqueConstraint(
      columnNames = {"id_historia_academica", "codigo_de_materia"}))
  @Builder.Default
  private Set<Materia> inscripcionesActuales = new HashSet<>();
}