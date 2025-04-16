package com.edu.asistenteCupos.service.factory;

import com.edu.asistenteCupos.Utils.PromptTemplateProvider;
import com.edu.asistenteCupos.domain.PeticionInscripcion;
import com.edu.asistenteCupos.domain.prompt.PromptBuilderTemplated;
import com.edu.asistenteCupos.repository.ComisionRepository;
import com.edu.asistenteCupos.repository.MateriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PromptFactory {
  private final MateriaRepository materiaRepository;
  private final ComisionRepository comisionRepository;
  private final PromptTemplateProvider templateProvider;

  @Value("${ai.model}")
  private String modelo;

  @Value("${ai.temperature:}")
  private double temperatura;

  public Prompt crearPrompt(List<PeticionInscripcion> peticiones) {
    return PromptBuilderTemplated.nuevo(templateProvider).conMaterias(materiaRepository.findAll())
                                 .conComisiones(comisionRepository.findAll())
                                 .conPeticionesDeInscripcion(peticiones).conTemperatura(temperatura)
                                 .conModelo(modelo).construir();
  }
}