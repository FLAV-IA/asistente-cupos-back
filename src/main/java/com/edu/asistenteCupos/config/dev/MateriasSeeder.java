package com.edu.asistenteCupos.config.dev;

import com.edu.asistenteCupos.Utils.ClasspathResourceLoader;
import com.edu.asistenteCupos.domain.Materia;
import com.edu.asistenteCupos.repository.MateriaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;

import java.util.*;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class MateriasSeeder {
  private final MateriaRepository materiaRepository;
  private final ClasspathResourceLoader resourceLoader;
  String nombreCsv = "materias.csv";

  public void cargarMaterias(String nombreArchivo) throws Exception {
    if (!materiaRepository.findAll().isEmpty()) {
      log.info("No se cargan materias porque ya existen.");
      return;
    }
    List<String[]> rows = resourceLoader.leerCSV(nombreArchivo, "\\|");
    Map<String, Materia> materias = crearMateriasDesde(rows);
    asociarCorrelativas(rows, materias);
    log.info("Se cargaron ${} materias.", materias.size());
  }

  @Bean
  @Order(1)
  @Profile({"dev", "test"})
  CommandLineRunner runMateriasSeeder() {
    return args -> cargarMaterias(nombreCsv);
  }

  private Map<String, Materia> crearMateriasDesde(List<String[]> rows) {
    Map<String, Materia> materias = new HashMap<>();
    for (String[] row : rows.stream().skip(1).toList()) {
      String nombre = row[0].trim();
      String codigo = row[1].trim();

      Materia materia = Materia.builder().nombre(nombre).codigo(codigo).build();

      materiaRepository.save(materia);

      materias.put(codigo, materia);
    }
    return materias;
  }

  private void asociarCorrelativas(List<String[]> rows, Map<String, Materia> materias) {
    for (String[] row : rows.stream().skip(1).toList()) {
      String codigo = row[1].trim();
      Materia materia = materias.get(codigo);

      if (row.length > 2 && !row[2].equals("-")) {
        List<Materia> correlativas = Arrays.stream(row[2].split(",")).map(String::trim)
                                           .map(materias::get).filter(Objects::nonNull).toList();

        materia.setCorrelativas(correlativas);
        materiaRepository.save(materia);
      }
    }
  }
}
