package com.edu.asistente_cupos.service.adapter;

import com.edu.asistente_cupos.controller.dto.PeticionInscripcionCsvDTO;
import com.edu.asistente_cupos.excepcion.ArchivoCsvInvalidoException;
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
  public static final char SEPARATOR = '|';

  public List<PeticionInscripcionCsvDTO> adapt(MultipartFile archivoCsv) {
    try (InputStreamReader reader = new InputStreamReader(archivoCsv.getInputStream(),
      StandardCharsets.UTF_8)) {

      return new CsvToBeanBuilder<PeticionInscripcionCsvDTO>(reader)
        .withType(PeticionInscripcionCsvDTO.class)
        .withIgnoreLeadingWhiteSpace(true)
        .withSeparator(SEPARATOR)
        .build()
        .parse();
    } catch (Exception e) {
      throw new ArchivoCsvInvalidoException("El archivo CSV no tiene el formato esperado", e);
    }
  }
}
