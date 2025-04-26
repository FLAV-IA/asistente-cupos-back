package com.edu.asistenteCupos.config.dev;

import com.edu.asistenteCupos.Utils.ClasspathResourceLoader;
import com.edu.asistenteCupos.domain.Estudiante;
import com.edu.asistenteCupos.domain.HistoriaAcademica;
import com.edu.asistenteCupos.domain.Materia;
import com.edu.asistenteCupos.repository.EstudianteRepository;
import com.edu.asistenteCupos.repository.MateriaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class EstudianteConHistoriaAcademicaSeeder {
  private final EstudianteRepository estudianteRepository;
  private final MateriaRepository materiaRepository;
  private final ClasspathResourceLoader resourceLoader;
  String nombreCsv = "csv/historiaAcademica.csv";

  public void cargarEstudiante(String nombreArchivo) throws Exception {
    List<String[]> rows = resourceLoader.leerCSV(nombreArchivo, "\\|");
    for (String[] row : rows.stream().skip(1).toList()) {
      Estudiante estudiante = obtenerEstudiante(row);
      HistoriaAcademica historiaAcademica = obtenerHistoriaAcademica(row);
      estudiante.setHistoriaAcademica(historiaAcademica);
      estudianteRepository.save(estudiante);
    }
    log.info("Se cargaron [{}] estudiantes con su historia academica.", rows.size() - 1);

  }

  private Estudiante obtenerEstudiante(String[] row) {
    String legajo = row[0].trim();
    String nombre = row[1].trim() + " " + row[2].trim();
    String mail = row[3].trim();
    return Estudiante.builder().legajo(legajo).mail(mail).nombre(nombre).build();
  }

  private HistoriaAcademica obtenerHistoriaAcademica(String[] row) {
    Double coeficiente = Double.parseDouble(row[4].trim().equals("-") ? "0" : row[4].trim());
    int inscTot = Integer.parseInt(row[5].trim());
    String inscAct = row[6].trim();
    String insc1C2024 = row[7].trim();
    String aprob1C2024 = row[8].trim();
    String insc2C2024 = row[9].trim();
    String aprob2C2024 = row[10].trim();

    Set<Materia> materiasAnotadas = inscAct.equals("-") ? new java.util.HashSet<>() : Arrays
      .stream(inscAct.split(",")).map(
        materia -> this.materiaRepository.findByCodigo(materia.replace('"', ' ').replace(" ", ""))
                                         .orElseThrow(() -> new RuntimeException(
                                           "No se encontro la materia con el codigo: " + row[0] +
                                             "materia" + materia))).collect(Collectors.toSet());
    return HistoriaAcademica.builder().inscTot(inscTot).aprob1c(aprob1C2024)
                            .aprob2c(aprob2C2024).curso1c(insc1C2024).curso2c(insc2C2024)
                            .coeficiente(coeficiente).anotadas(materiasAnotadas).build();

  }

  @Bean
  @Order(3)
  @Profile({"dev", "test"})
  CommandLineRunner runHistoriaAcademicaSeeder() {
    return args -> cargarEstudiante(nombreCsv);
  }
}
