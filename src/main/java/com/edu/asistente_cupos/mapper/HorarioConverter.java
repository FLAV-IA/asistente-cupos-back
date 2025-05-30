package com.edu.asistente_cupos.mapper;

import com.edu.asistente_cupos.domain.horario.Horario;
import com.edu.asistente_cupos.domain.horario.HorarioParser;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class HorarioConverter implements AttributeConverter<Horario, String> {

  @Override
  public String convertToDatabaseColumn(Horario horario) {
    return (horario == null || horario.estaVacio()) ? "" : horario.toString();
  }

  @Override
  public Horario convertToEntityAttribute(String dbData) {
    return HorarioParser.parse(dbData);
  }
}
