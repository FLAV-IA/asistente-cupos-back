package com.edu.asistente_cupos.controller.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

@Data
public class PeticionInscripcionCsvDTO {
  @CsvBindByName(column = "dni")
  private String dni;

  @CsvBindByName(column = "codigos_comisiones")
  private String codigosComisiones;
}
