package com.edu.asistenteCupos.controller.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

@Data
public class PeticionInscripcionCsvDTO {
  @CsvBindByName(column = "legajo")
  private String legajo;

  @CsvBindByName(column = "codigo_comision")
  private String codigoComision;
}
