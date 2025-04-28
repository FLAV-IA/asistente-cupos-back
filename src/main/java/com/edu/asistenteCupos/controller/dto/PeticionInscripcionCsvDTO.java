package com.edu.asistenteCupos.controller.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

@Data
public class PeticionInscripcionCsvDTO {
  @CsvBindByName
  private String nombre;

  @CsvBindByName(column = "legajo")
  private String legajo;

  @CsvBindByName(column = "materia")
  private String materia;

  @CsvBindByName(column = "comision")
  private String comisiones;

  @CsvBindByName(column = "codigoMateria")
  private String codigoMateria;
  @CsvBindByName
  private boolean correlativa;

  @CsvBindByName
  private int insc3;

  @CsvBindByName
  private int inscAct;

  @CsvBindByName
  private int aprobUlt;

  @CsvBindByName
  private int inscTot;

  @CsvBindByName
  private int aprobTot;

  @CsvBindByName
  private int restantes;

}
