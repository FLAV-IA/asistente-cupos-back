package com.edu.asistente_cupos.utils;

import com.edu.asistente_cupos.Utils.ClasspathResourceLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;
import java.io.IOException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ClasspathResourceLoaderTest {
  private ClasspathResourceLoader loader;

  @BeforeEach
  void setUp() {
    loader = new ClasspathResourceLoader();
  }

  @Test
  void debeLeerLineasDesdeUnArchivo() throws IOException {
    List<String> lineas = loader.leerLineas("lineas.txt");

    assertEquals(3, lineas.size());
    assertEquals("Primera línea", lineas.get(0));
    assertEquals("Segunda línea", lineas.get(1));
    assertEquals("Tercera línea", lineas.get(2));
  }

  @Test
  void debeLeerCSVDesdeUnArchivo() throws IOException {
    List<String[]> datos = loader.leerCSV("archivo.csv", ",");

    assertEquals(2, datos.size());
    assertArrayEquals(new String[]{"Juan", "20"}, datos.get(0));
    assertArrayEquals(new String[]{"Ana", "22"}, datos.get(1));
  }

  @Test
  void debeLeerArchivoComoTextoPlano() throws IOException {
    String contenido = loader.leerComoTexto("lineas.txt");

    assertTrue(contenido.contains("Primera línea"));
    assertTrue(contenido.contains("Tercera línea"));
  }

  @Test
  void debeObtenerResourceDesdeClasspath() {
    Resource resource = loader.comoRecurso("lineas.txt");

    assertNotNull(resource);
    assertTrue(resource.exists());
  }

  @Test
  void debeLanzarExcepcionSiArchivoNoExiste() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      loader.leerLineas("no-existe.txt");
    });

    assertTrue(exception.getMessage().contains("no encontrado"));
  }
}
