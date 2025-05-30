package com.edu.asistente_cupos.domain.cursada;

import com.edu.asistente_cupos.domain.Materia;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Cursada {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne
  private Materia materia;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, optional = false)
  private EstadoCursada estado;

  public boolean getFueAprobada() {
    return estado.fueAprobada();
  }
}
