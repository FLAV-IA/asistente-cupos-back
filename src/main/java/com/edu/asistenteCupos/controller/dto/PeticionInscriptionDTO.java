package com.edu.asistenteCupos.controller.dto;

import lombok.Data;

import java.util.List;

@Data
public class PeticionInscriptionDTO {
  String nombre;
  String legajo;
  String materia;
  List<String> comisiones;
  HistoriaAcademicaDTO historiaAcademica;
  boolean correlativa;
}