package com.edu.asistenteCupos.Utils.prompt;

import com.edu.asistenteCupos.Utils.ClasspathResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class PromptTemplateProvider {
  private ClasspathResourceLoader loader;

  public PromptTemplateProvider(ClasspathResourceLoader loader) {
    this.loader = loader;
  }

  public Resource getTemplate(String path) {
    return loader.comoRecurso(path);
  }

  public String leerTemplateComoString(String path) {
    try {
      return StreamUtils.copyToString(getTemplate(path).getInputStream(), StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new RuntimeException("No se pudo leer el template: " + path, e);
    }
  }
}