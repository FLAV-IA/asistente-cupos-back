package com.edu.asistenteCupos.controller;

import com.edu.asistenteCupos.assembler.EnsambladorDePeticiones;
import com.edu.asistenteCupos.controller.dto.PeticionInscripcionCsvDTO;
import com.edu.asistenteCupos.controller.dto.SugerenciaInscripcionDTO;
import com.edu.asistenteCupos.domain.peticion.PeticionInscripcion;
import com.edu.asistenteCupos.domain.sugerencia.SugerenciaInscripcion;
import com.edu.asistenteCupos.domain.filtros.FiltroDePeticionInscripcion;
import com.edu.asistenteCupos.mapper.SugerenciaInscripcionMapper;
import com.edu.asistenteCupos.service.AsistenteDeInscripcion;
import com.edu.asistenteCupos.service.adapter.PeticionInscripcionCsvAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/asistente")
@RequiredArgsConstructor
@Slf4j
public class AsistenteController {
  private final AsistenteDeInscripcion asistenteDeInscripcion;
  private final SugerenciaInscripcionMapper sugerenciaInscripcionMapper;
  private final PeticionInscripcionCsvAdapter peticionInscripcionCsvAdapter;
  private final EnsambladorDePeticiones ensambladorDePeticiones;
  private final FiltroDePeticionInscripcion filtroDePeticionInscripcion;

  @PostMapping("/sugerencia-inscripcion-con-csv")
  public ResponseEntity<List<SugerenciaInscripcionDTO>> sugerirInscripcionConCsv(
    @RequestParam(value = "file", required = false) MultipartFile file) {
    try {
      if (file == null || file.isEmpty()) {
        return ResponseEntity.badRequest().body(List.of());
      }

      List<PeticionInscripcionCsvDTO> peticionesCSV = peticionInscripcionCsvAdapter.adapt(file);
      List<PeticionInscripcion> peticiones = ensambladorDePeticiones.ensamblarDesdeCsvDto(
        peticionesCSV);
        List<PeticionInscripcion> peticionesFiltradas = filtroDePeticionInscripcion.filtrar(peticiones);
      List<SugerenciaInscripcion> sugerencias = asistenteDeInscripcion.sugerirInscripcion(
              peticionesFiltradas);
      List<SugerenciaInscripcionDTO> sugerenciasDTO = sugerenciaInscripcionMapper.toSugerenciaInscripcionDtoList(
        sugerencias);
      return ResponseEntity.ok(sugerenciasDTO);

    } catch (Exception e) {
      log.error("Error procesando CSV", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of());
    }
  }
}