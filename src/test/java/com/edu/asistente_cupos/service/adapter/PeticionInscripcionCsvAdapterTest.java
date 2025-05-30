package com.edu.asistente_cupos.service.adapter;

import com.edu.asistente_cupos.controller.dto.PeticionInscripcionCsvDTO;
import com.edu.asistente_cupos.excepcion.ArchivoCsvInvalidoException;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PeticionInscripcionCsvAdapterTest {
  PeticionInscripcionCsvAdapter adapter = new PeticionInscripcionCsvAdapter();

  @Test
  void parseaCorrectamenteArchivoCsvValido() {
    String contenido = "dni|codigos_comisiones\n12345678|COM1,COM2";
    MockMultipartFile archivo = new MockMultipartFile("archivo", "inscripciones.csv", "text/csv",
      contenido.getBytes(StandardCharsets.UTF_8));

    List<PeticionInscripcionCsvDTO> resultado = adapter.adapt(archivo);

    assertThat(resultado).hasSize(1);
    PeticionInscripcionCsvDTO dto = resultado.get(0);
    assertThat(dto.getDni()).isEqualTo("12345678");
    assertThat(dto.getCodigosComisiones()).isEqualTo("COM1,COM2");
  }

  @Test
  void lanzaExcepcionSiElArchivoEsInvalido() {
    MockMultipartFile archivoInvalido = new MockMultipartFile("archivo", "inscripciones.csv",
      "text/csv", "dni|codigos_comisiones\nINVALID_LINE".getBytes());

    assertThrows(ArchivoCsvInvalidoException.class, () -> adapter.adapt(archivoInvalido));
  }

  @Test
  void lanzaExcepcionSiNoPuedeLeerseElArchivo() throws IOException {
    MultipartFile archivoMock = mock(MultipartFile.class);
    when(archivoMock.getInputStream()).thenThrow(new IOException("falló"));

    assertThrows(ArchivoCsvInvalidoException.class, () -> adapter.adapt(archivoMock));
  }
}
