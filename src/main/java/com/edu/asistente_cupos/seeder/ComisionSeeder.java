package com.edu.asistente_cupos.seeder;

import com.edu.asistente_cupos.Utils.ClasspathResourceLoader;
import com.edu.asistente_cupos.domain.Comision;
import com.edu.asistente_cupos.domain.Materia;
import com.edu.asistente_cupos.domain.horario.Horario;
import com.edu.asistente_cupos.domain.horario.HorarioParser;
import com.edu.asistente_cupos.repository.ComisionRepository;
import com.edu.asistente_cupos.repository.MateriaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;

import java.util.List;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ComisionSeeder {
  private final ComisionRepository comisionRepository;
  private final MateriaRepository materiaRepository;
  private final ClasspathResourceLoader resourceLoader;
  private final String nombreCsv = "csv/comisiones.csv";

  public void cargarComisiones(String nombreArchivo) throws Exception {
    log.info("Comienza la carga de comisiones desde [{}].", nombreCsv);
    if (!comisionRepository.findAll().isEmpty()) {
      log.info("No se cargan comisiones porque ya existen.");
      return;
    }
    List<String[]> rows = resourceLoader.leerCSV(nombreArchivo, "\\|").stream().skip(1).toList();

    for (String[] row : rows) {
      String codigoMateria = row[0];
      String codigoComision = row[1];
      String descripcionHorario = row[2];
      int cupo = Integer.parseInt(row[3]);

      Optional<Materia> materiaOpt = materiaRepository.findByCodigo(codigoMateria);

      String codigoMateriaComision = codigoMateria + "-" + codigoComision;
      if (materiaOpt.isPresent()) {
        Materia materia = materiaOpt.get();
        Horario horario = HorarioParser.parse(descripcionHorario);

        Comision comision = Comision.builder().codigo(codigoMateriaComision).materia(materia)
                                    .horario(horario).cupo(cupo).build();

        comisionRepository.save(comision);
      } else {
        String mensaje = String.format(
          "Materia con código [%s] no encontrada para la comisión [%s - %n]", codigoMateria,
          codigoMateriaComision);
        log.error(mensaje);
      }
    }
    log.info("Se cargaron [{}] comisiones.", rows.size() - 1);
  }

  @Bean
  @Order(2)
  @Profile({"dev", "test"})
  @Transactional
  CommandLineRunner runComisionSeeder() {
    return args -> cargarComisiones(nombreCsv);
  }
}
