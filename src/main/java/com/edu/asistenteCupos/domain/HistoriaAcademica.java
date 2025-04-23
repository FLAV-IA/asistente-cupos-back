package com.edu.asistenteCupos.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
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
  @JoinColumn(name = "legajo_estudiante", referencedColumnName = "legajo", unique = true)
  private Estudiante estudiante;

  private int cantInscripciones3CursadasPrevias;
  private int cantAprobadas3CursadasPrevias;
  private int cantInscripcionesHistoricas;
  private int cantMateriasAprobadasHistoricas;
  private int cantMateriasRestantes;
  private Boolean cumpleCorrelatividad;
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
          name = "historia_academica_anotadas",
          joinColumns = @JoinColumn(name = "historia_academica_id_historia_academica"),
          inverseJoinColumns = @JoinColumn(name = "anotadas_codigo"),
          uniqueConstraints = @UniqueConstraint(columnNames = {"historia_academica_id_historia_academica", "anotadas_codigo"})
  )
  private Set<Materia> materiasActuales = new HashSet<>();

}