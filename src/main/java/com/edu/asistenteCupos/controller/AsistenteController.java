package com.edu.asistenteCupos.controller;

import com.edu.asistenteCupos.controller.dto.PeticionesInscripcionDTO;
import com.edu.asistenteCupos.controller.dto.SugerenciaInscripcionDto;
import com.edu.asistenteCupos.domain.PeticionInscripcion;
import com.edu.asistenteCupos.domain.SugerenciaInscripcion;
import com.edu.asistenteCupos.mapper.PeticionInscripcionMapper;
import com.edu.asistenteCupos.mapper.SugerenciaInscripcionMapper;
import com.edu.asistenteCupos.service.AsistenteDeInscripcion;
import com.edu.asistenteCupos.service.MantenedorAlumno;
import com.edu.asistenteCupos.service.adapter.PeticionInscripcionCsvAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/asistente")
@RequiredArgsConstructor
class AsistenteController {
  private final AsistenteDeInscripcion asistenteDeInscripcion;
  private final SugerenciaInscripcionMapper sugerenciaInscripcionMapper;
  private final PeticionInscripcionMapper peticionInscripcionMapper;
  private final PeticionInscripcionCsvAdapter peticionInscripcionCsvAdapter;
  private final MantenedorAlumno mantenedorAlumno;

  @PostMapping("/sugerencia-inscripcion-con-csv")
  public ResponseEntity<List<SugerenciaInscripcionDto>> sugerirInscripcionConCsv(@RequestParam(required = false) MultipartFile file) {
    try {
      List<PeticionInscripcion> peticiones = peticionInscripcionCsvAdapter.convertir(file);
      List<SugerenciaInscripcion> sugerencias = asistenteDeInscripcion.sugerirInscripcion(
        peticiones);
      List<SugerenciaInscripcionDto> sugerenciasDTO = sugerenciaInscripcionMapper.toSugerenciaInscripcionDtoList(
        sugerencias);
      return ResponseEntity.ok(sugerenciasDTO);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
    }
  }

  @PostMapping("/sugerencia-inscripcion")
  public ResponseEntity<List<SugerenciaInscripcionDto>> sugerirInscripcion(@RequestBody PeticionesInscripcionDTO peticionesDTO) {
    try {
      List<PeticionInscripcion> peticiones = peticionInscripcionMapper.toPeticionInscripcionList(
        peticionesDTO.peticiones());
      List<SugerenciaInscripcion> sugerencias = asistenteDeInscripcion.sugerirInscripcion(
        peticiones);
      List<SugerenciaInscripcionDto> sugerenciasDTO = sugerenciaInscripcionMapper.toSugerenciaInscripcionDtoList(
        sugerencias);
      return ResponseEntity.ok(sugerenciasDTO);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
    }
  }

  @PostMapping("/ver-prompt")
  public ResponseEntity<String> verPrompt(@RequestParam(required = false) MultipartFile file) {
    try {
      List<PeticionInscripcion> peticiones = peticionInscripcionCsvAdapter.convertir(file);
      return ResponseEntity.ok(asistenteDeInscripcion.mostrarPrompt(peticiones));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                           .body("Error en la consulta: " + e.getMessage());
    }
  }
}