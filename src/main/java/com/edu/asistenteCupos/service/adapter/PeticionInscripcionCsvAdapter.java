package com.edu.asistenteCupos.service.adapter;

import com.edu.asistenteCupos.controller.dto.PeticionInscripcionCsvDTO;
import com.edu.asistenteCupos.domain.PeticionInscripcion;
import com.edu.asistenteCupos.mapper.PeticionInscripcionMapper;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PeticionInscripcionCsvAdapter {
  private final PeticionInscripcionMapper peticionInscripcionMapper;

  public List<PeticionInscripcion> convertir(MultipartFile archivoCsv) {
    try (InputStreamReader reader = new InputStreamReader(archivoCsv.getInputStream(),
      StandardCharsets.UTF_8)) {
      List<PeticionInscripcionCsvDTO> dtos = new CsvToBeanBuilder<PeticionInscripcionCsvDTO>(reader)
              .withType(PeticionInscripcionCsvDTO.class)
              .withIgnoreLeadingWhiteSpace(true)
              .build()
              .parse();

      return peticionInscripcionMapper.toPeticionInscripcionCsvList(dtos);
    } catch (Exception e) {
      throw new RuntimeException("Error al procesar el archivo CSV: " + e.getMessage(), e);
    }
  }
}
