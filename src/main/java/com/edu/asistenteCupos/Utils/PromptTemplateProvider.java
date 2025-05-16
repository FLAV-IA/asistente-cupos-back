package com.edu.asistenteCupos.Utils;

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

  public Resource systemResource() {
    String systemPathName = "prompt/system-template.md";
    return loader.comoRecurso(systemPathName);
  }

  public Resource userResource() {
    String userPathName = "prompt/user-template.md";
    return loader.comoRecurso(userPathName);
  }

  public String metodoNelson() {
    String templateString;
    try {
      templateString = StreamUtils.copyToString(systemResource().getInputStream(),
        StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return templateString;
  }
}