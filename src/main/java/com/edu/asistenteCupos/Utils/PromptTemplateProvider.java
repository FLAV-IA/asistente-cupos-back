package com.edu.asistenteCupos.Utils;

import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

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
}