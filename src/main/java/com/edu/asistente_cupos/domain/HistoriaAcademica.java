package com.edu.asistente_cupos.domain;

import com.edu.asistente_cupos.domain.cursada.Cursada;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
  private List<Cursada> cursadas;

  public List<Cursada> inscripcionesActuales() {
    return cursadas == null ? List.of() : cursadas.stream().filter(c -> c.getEstado().estaEnCurso())
                                                  .toList();
  }

  public List<Cursada> cursadasAnteriores() {
    return
      cursadas == null ? List.of() : cursadas.stream().filter(c -> !c.getEstado().estaEnCurso())
                                             .toList();
  }

  public Boolean cumpleCorrelativas(Materia materia) {
    if (cursadas == null)
      return false;

    List<Materia> correlativasNecesarias = materia.getCorrelativas();
    if (correlativasNecesarias == null || correlativasNecesarias.isEmpty()) {
      return true;
    }

    return correlativasNecesarias.stream().allMatch(
      correlativa -> cursadas.stream().filter(Cursada::fueAprobada).anyMatch(
        cursada -> cursada.getMateria().getCodigo().equals(correlativa.getCodigo())));
  }

  public boolean haySuperposicionHoraria(Comision nuevaComision) {
    return !materiasEnCurso().isEmpty();
  }

  public List<Materia> materiasEnCurso() {
    return cursadas == null ? List.of() : cursadas.stream().filter(c -> c.getEstado().estaEnCurso())
                                                  .map(Cursada::getMateria).toList();
  }
}