package com.edu.asistente_cupos.seeder;

import com.edu.asistente_cupos.Utils.ClasspathResourceLoader;
import com.edu.asistente_cupos.domain.Estudiante;
import com.edu.asistente_cupos.domain.HistoriaAcademica;
import com.edu.asistente_cupos.domain.Materia;
import com.edu.asistente_cupos.domain.cursada.Cursada;
import com.edu.asistente_cupos.domain.cursada.CursadaFactory;
import com.edu.asistente_cupos.repository.ComisionRepository;
import com.edu.asistente_cupos.repository.EstudianteRepository;
import com.edu.asistente_cupos.repository.MateriaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class EstudianteConHistoriaAcademicaSeeder {
  private final EstudianteRepository estudianteRepository;
  private final MateriaRepository materiaRepository;
  private final ComisionRepository comisionRepository;
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
    String dni = row[0].trim();
    String nombre = row[1].trim() + " " + row[2].trim();
    String mail = row[3].trim();
    return Estudiante.builder().dni(dni).mail(mail).nombre(nombre).build();
  }

  private HistoriaAcademica obtenerHistoriaAcademica(String[] row) {
    Double coeficiente = Double.parseDouble(row[4].trim().equals("-") ? "0" : row[4].trim());
    int totalInscripcionesHistoricas = Integer.parseInt(row[5].trim());
    String inscripcionesActuales = row[6].trim();
    int totalHistoricasAprobadas = Integer.parseInt((row.length==11|| row[11].trim().equals("-") ) ? "0" : row[11].trim());

    List<Cursada> cursadasActuales = parsearMaterias(inscripcionesActuales, Collectors.toList()).stream()
                                                                                        .map(
                                                                                          CursadaFactory::enCurso)
                                                                                        .toList();

    List<Cursada> cursadasAnterioresC12024 = construirCursadas(row[7].trim(), row[8].trim());
    List<Cursada> cursadasAnterioresC22024 = construirCursadas(row[9].trim(), row[10].trim());

    List<Cursada> cursadasAnteriores = Stream
      .of(cursadasAnterioresC12024, cursadasAnterioresC22024, cursadasActuales)
      .flatMap(Collection::stream).toList();

    return HistoriaAcademica.builder().totalInscripcionesHistoricas(totalInscripcionesHistoricas)
                            .totalHistoricasAprobadas(totalHistoricasAprobadas)
                            .coeficiente(coeficiente).cursadas(cursadasAnteriores).build();
  }

  private List<Cursada> construirCursadas(String inscriptas, String aprobadas) {
    List<Materia> materiasCursadas = parsearMaterias(inscriptas, Collectors.toList());
    List<Materia> materiasAprobadas = parsearMaterias(aprobadas, Collectors.toList());

    return materiasCursadas.stream().map(
      materia -> materiasAprobadas.contains(materia) ? CursadaFactory.aprobada(materia,
        10) : CursadaFactory.desaprobada(materia, 2)).toList();
  }

  private <T extends Collection<Materia>> T parsearMaterias(String materias, Collector<Materia, ?, T> collector) {
    return (materias.equals("-") ? Stream.<Materia>empty() : Arrays
      .stream(removerCaracteresEspeciales(materias).split(",")).map(
        codigoMateria -> materiaRepository.findByCodigo(codigoMateria).orElseThrow(
          () -> new RuntimeException(
            "No se encontró la materia con el código: " + codigoMateria)))).collect(collector);
  }

  private String removerCaracteresEspeciales(String texto) {
    return texto.replaceAll("[\"\\s]", "").trim();
  }

  @Bean
  @Order(3)
  @Profile({"dev", "test"})
  CommandLineRunner runHistoriaAcademicaSeeder() {
    return args -> cargarEstudiante(nombreCsv);
  }
}
