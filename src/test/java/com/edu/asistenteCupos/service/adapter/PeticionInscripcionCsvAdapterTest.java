package com.edu.asistenteCupos.service.adapter;

import com.edu.asistenteCupos.domain.PeticionInscripcion;
import com.edu.asistenteCupos.mapper.PeticionInscripcionMapper;
import com.edu.asistenteCupos.mapper.PeticionInscripcionMapperImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PeticionInscripcionCsvAdapterTest {
  private final PeticionInscripcionMapper mapper = new PeticionInscripcionMapperImpl();
  private final PeticionInscripcionCsvAdapter adapter = new PeticionInscripcionCsvAdapter(mapper);
  @TempDir
  Path tempDir;

  @Test
  void convierteArchivoCsvEnUnaListaDePeticiones() throws Exception {
    MockMultipartFile archivo = generarArchivo("45072112,Bases de Datos,\"C1,C2\",1035",
      "40946592,Matematica 1,\"C2\",8003N");

    List<PeticionInscripcion> peticiones = adapter.convertir(archivo);

    assertThat(peticiones).hasSize(2);
    assertThat(peticiones.get(0).getMateria()).isEqualTo("Bases de Datos");
    assertThat(peticiones.get(1).getEstudiante().getLegajo()).isEqualTo("40946592");
  }

  private MockMultipartFile generarArchivo(String unEstudiante, String otroEstudiante) throws IOException {
    File tempCsv = tempDir.resolve("peticiones.csv").toFile();
    try (FileWriter writer = new FileWriter(tempCsv)) {
      writer.write("legajo,materia,comision,codigoMateria\n");
      writer.write(unEstudiante + "\n");
      writer.write(otroEstudiante + "\n");
    }
    return new MockMultipartFile("file", "peticiones.csv", "text/csv",
      java.nio.file.Files.readAllBytes(tempCsv.toPath()));
  }
}
