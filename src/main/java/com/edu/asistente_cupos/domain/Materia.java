package com.edu.asistente_cupos.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Materia {
  @Id
  private String codigo;

  private String nombre;

  @ManyToMany
  @Builder.Default
  private List<Materia> correlativas = new ArrayList<>();
}