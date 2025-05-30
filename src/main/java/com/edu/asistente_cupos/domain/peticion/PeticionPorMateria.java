package com.edu.asistente_cupos.domain.peticion;

import com.edu.asistente_cupos.domain.Comision;
import com.edu.asistente_cupos.domain.Materia;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PeticionPorMateria {
  private List<Comision> comisiones;
  private boolean cumpleCorrelativa;

  public Materia getMateria() {
    if (comisiones == null || comisiones.isEmpty()) {
      throw new IllegalStateException("Petición sin comisiones asociadas");
    }
    return comisiones.get(0).getMateria();
  }

  public String getCodigoMateria() {
    return getMateria().getCodigo();
  }
}
