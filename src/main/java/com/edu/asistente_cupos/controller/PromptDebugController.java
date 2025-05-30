package com.edu.asistente_cupos.controller;

import com.edu.asistente_cupos.assembler.EnsambladorDePeticiones;
import com.edu.asistente_cupos.controller.dto.PeticionInscripcionCsvDTO;
import com.edu.asistente_cupos.domain.peticion.PeticionInscripcion;
import com.edu.asistente_cupos.observacion.VistaDePrompt;
import com.edu.asistente_cupos.service.adapter.PeticionInscripcionCsvAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/debug")
public class PromptDebugController {
  private final PeticionInscripcionCsvAdapter peticionInscripcionCsvAdapter;
  private final EnsambladorDePeticiones ensambladorDePeticiones;
  private final VistaDePrompt vistaDePrompt;

  @PostMapping("/prompt")
  public ResponseEntity<String> verPrompt(@RequestParam(required = false) MultipartFile file) {
    try {
      List<PeticionInscripcionCsvDTO> peticionesCSV = peticionInscripcionCsvAdapter.adapt(file);
      List<PeticionInscripcion> peticiones = ensambladorDePeticiones.ensamblarDesdeCsvDto(
        peticionesCSV);
      return ResponseEntity.ok(vistaDePrompt.mostrarPromptBonito(peticiones));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                           .body("Error en la consulta: " + e.getMessage());
    }
  }
}
