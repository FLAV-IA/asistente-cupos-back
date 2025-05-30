package com.edu.asistente_cupos.Utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ClasspathResourceLoader {
  public List<String> leerLineas(String path) throws IOException {
    try (BufferedReader reader = crearBufferedReader(path)) {
      return reader.lines().filter(line -> !line.isBlank()).collect(Collectors.toList());
    }
  }

  public List<String[]> leerCSV(String path, String separador) throws IOException {
    try (BufferedReader reader = crearBufferedReader(path)) {
      return reader.lines().filter(line -> !line.isBlank()).map(line -> line.split(separador))
                   .peek(arr -> {
                     for (int i = 0; i < arr.length; i++) {
                       arr[i] = arr[i].trim();
                     }
                   }).toList();
    }
  }

  public String leerComoTexto(String path) throws IOException {
    return String.join("\n", leerLineas(path));
  }

  public Resource comoRecurso(String path) {
    return new ClassPathResource(path);
  }

  private BufferedReader crearBufferedReader(String path) throws IOException {
    Resource resource = new ClassPathResource(path);
    if (!resource.exists()) {
      throw new IllegalArgumentException("Archivo [%s] no encontrado".formatted(path));
    }

    return new BufferedReader(
      new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));
  }
}
