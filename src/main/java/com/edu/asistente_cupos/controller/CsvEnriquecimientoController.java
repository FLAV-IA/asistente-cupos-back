package com.edu.asistente_cupos.controller;

import com.edu.asistente_cupos.assembler.EnsambladorDePeticiones;
import com.edu.asistente_cupos.controller.dto.PeticionInscripcionDTO;
import com.edu.asistente_cupos.mapper.PeticionPrevisualizacionMapper;
import com.edu.asistente_cupos.service.adapter.PeticionInscripcionCsvAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/csv")
@RequiredArgsConstructor
@Slf4j
public class CsvEnriquecimientoController {
  private final PeticionInscripcionCsvAdapter peticionInscripcionCsvAdapter;
  private final EnsambladorDePeticiones ensambladorDePeticiones;
  private final PeticionPrevisualizacionMapper mapper;

  @PostMapping("/previsualizar")
  public ResponseEntity<List<PeticionInscripcionDTO>> previsualizarDesdeCsv(
    @RequestParam("file") MultipartFile file) {

    if (file == null || file.isEmpty()) {
      return ResponseEntity.badRequest().body(List.of());
    }

    var peticionesCSV = peticionInscripcionCsvAdapter.adapt(file);
    var peticiones = ensambladorDePeticiones.ensamblarDesdeCsvDto(peticionesCSV);
    List<PeticionInscripcionDTO> dtos = peticiones.stream().map(mapper::toDto).toList();

    return ResponseEntity.ok(dtos);
  }
}
