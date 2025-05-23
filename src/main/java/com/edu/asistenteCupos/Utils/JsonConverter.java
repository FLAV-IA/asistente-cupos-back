package com.edu.asistenteCupos.Utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.List;

public class JsonConverter {
  private static final ObjectMapper mapper = new ObjectMapper().enable(
    SerializationFeature.INDENT_OUTPUT);

  public static String toJson(Object peticiones) {
    try {
      return mapper.writeValueAsString(peticiones);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Error al convertir a JSON", e);
    }
  }

  public static <T> List<T> readList(String json, TypeReference<List<T>> typeRef) {
    try {
      return mapper.readValue(json, typeRef);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Error al convertir JSON a lista de objetos", e);
    }
  }
}
