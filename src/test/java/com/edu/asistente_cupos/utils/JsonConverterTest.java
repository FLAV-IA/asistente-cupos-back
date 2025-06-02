package com.edu.asistente_cupos.utils;

import com.edu.asistente_cupos.Utils.JsonConverter;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JsonConverterTest {
  @Test
  void toJsonSerializaDummyMateriaCorrectamente() {
    DummyMateria materia = new DummyMateria("MAT101", "Análisis I", List.of("MAT001"));
    String json = JsonConverter.toJson(materia);

    assertThat(json).contains("MAT101", "Análisis I", "MAT001");
  }

  @Test
  void readListConvierteJsonAListaDeDummyMateria() {
    String json = """
      [
        { "codigo": "MAT1", "nombre": "Intro", "correlativas": ["MAT0"] },
        { "codigo": "MAT2", "nombre": "Avanzada", "correlativas": ["MAT1"] }
      ]
      """;

    List<DummyMateria> lista = JsonConverter.readList(json, new TypeReference<>() {});
    assertEquals(2, lista.size());
    assertEquals("MAT1", lista.get(0).codigo());
    assertThat(lista.get(1).correlativas()).containsExactly("MAT1");
  }

  @Test
  void toJsonSerializaMapaConPalabrasDelDominio() {
    var materiaMap = Map.of("codigo", "MAT202", "nombre", "Física I", "correlativas",
      List.of("MAT100", "MAT101"));

    String json = JsonConverter.toJson(materiaMap);

    assertThat(json).contains("MAT202", "Física I", "MAT100", "MAT101");
  }

  @Test
  void readListConvierteJsonAListaDeMapas() {
    String json = """
      [
        { "codigo": "MAT3", "nombre": "Química", "correlativas": ["MAT1"] }
      ]
      """;

    List<Map<String, Object>> lista = JsonConverter.readList(json, new TypeReference<>() {});

    assertEquals(1, lista.size());
    assertEquals("MAT3", lista.get(0).get("codigo"));
  }

  @Test
  void toJsonLanzaExcepcionSiObjetoNoSerializable() {
    Object circular = new Object() {
      private final Object self = this;
    };

    RuntimeException ex = assertThrows(RuntimeException.class,
      () -> JsonConverter.toJson(circular));

    assertThat(ex.getMessage()).contains("Error al convertir a JSON");
  }

  @Test
  void readListLanzaExcepcionSiJsonEsInvalido() {
    String jsonInvalido = "[{codigo: sin_comillas}]";

    RuntimeException ex = assertThrows(RuntimeException.class,
      () -> JsonConverter.readList(jsonInvalido,
        new TypeReference<List<Map<String, Object>>>() {}));

    assertThat(ex.getMessage()).contains("Error al convertir JSON a lista de objetos");
  }

  record DummyMateria(String codigo, String nombre, List<String> correlativas) {}
}
