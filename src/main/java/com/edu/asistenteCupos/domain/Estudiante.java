package com.edu.asistenteCupos.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Estudiante {
  @Id
  private String dni;
  private String legajo;
  private String nombre;
  private String mail;
  @OneToOne(mappedBy = "estudiante", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonManagedReference
  private HistoriaAcademica historiaAcademica;

  public void setHistoriaAcademica(HistoriaAcademica historia) {
    this.historiaAcademica = historia;
    historia.setEstudiante(this);
  }

  public boolean puedeInscribirse(Materia materia) {
    return true;
  }
}
