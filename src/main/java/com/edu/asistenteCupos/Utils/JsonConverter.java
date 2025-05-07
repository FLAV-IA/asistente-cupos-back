package com.edu.asistenteCupos.Utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.List;
import java.util.Map;

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

  public static List<Map<String, Object>> readValue(String json) {
    try {
      var node = mapper.readTree(json);

      if (node.isArray()) {
        return mapper.readValue(json, new TypeReference<>() {});
      } else if (node.isObject()) {
        Map<String, Object> single = mapper.convertValue(node, new TypeReference<>() {});
        return List.of(single);
      } else {
        throw new RuntimeException("El JSON no tiene formato de objeto ni de array.");
      }

    } catch (JsonProcessingException e) {
      throw new RuntimeException("Error al parsear el JSON: " + e.getMessage(), e);
    }
  }

  public static <T> T convertMap(Map<String, Object> map, Class<T> clazz) {
    return mapper.convertValue(map, clazz);
  }
}