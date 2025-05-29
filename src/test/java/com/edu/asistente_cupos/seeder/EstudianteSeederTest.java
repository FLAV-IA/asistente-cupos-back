package com.edu.asistente_cupos.seeder;

import com.edu.asistente_cupos.Utils.ClasspathResourceLoader;
import com.edu.asistente_cupos.domain.Comision;
import com.edu.asistente_cupos.domain.Estudiante;
import com.edu.asistente_cupos.domain.Materia;
import com.edu.asistente_cupos.repository.EstudianteRepository;
import com.edu.asistente_cupos.repository.MateriaRepository;
import com.edu.asistente_cupos.repository.impl.memory.ComisionRepositoryInMemory;
import com.edu.asistente_cupos.repository.impl.memory.EstudianteRepositoryInMemory;
import com.edu.asistente_cupos.repository.impl.memory.MateriaRepositoryInMemory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EstudianteSeederTest {
  @Test
  void cargaAlumnoConHistoriaAcademicaDesdeCSVDeTest() throws Exception {
    MateriaRepository materiaRepo = new MateriaRepositoryInMemory();
    materiaRepo.save(Materia.builder().codigo("646").nombre("Intro Test").build());
    materiaRepo.save(Materia.builder().codigo("487").nombre("Intro Test").build());
    materiaRepo.save(Materia.builder().codigo("1038").nombre("Intro Test").build());
    materiaRepo.save(Materia.builder().codigo("1035").nombre("Intro Test").build());
    materiaRepo.save(Materia.builder().codigo("1041").nombre("Intro Test").build());
    materiaRepo.save(Materia.builder().codigo("1042").nombre("Intro Test").build());
    materiaRepo.save(Materia.builder().codigo("1048").nombre("Intro Test").build());
    materiaRepo.save(Materia.builder().codigo("1046").nombre("Avanzado Test").build());

    EstudianteRepository estudianteRepo = new EstudianteRepositoryInMemory();
    ComisionRepositoryInMemory comisionRepo = new ComisionRepositoryInMemory();
    comisionRepo.save(
      Comision.builder().codigo("1046").materia(materiaRepo.findByCodigo("1046").orElseThrow())
              .build());
    comisionRepo.save(
      Comision.builder().codigo("1041").materia(materiaRepo.findByCodigo("1041").orElseThrow())
              .build());

    EstudianteConHistoriaAcademicaSeeder seeder = new EstudianteConHistoriaAcademicaSeeder(
      estudianteRepo, materiaRepo, comisionRepo, new ClasspathResourceLoader());

    seeder.cargarEstudiante("csv/historiaAcademica_correcta.csv");

    List<Estudiante> all = estudianteRepo.findAll();
    assertThat(all).hasSize(2);
    Estudiante estudiante = estudianteRepo.findByDni("100").orElseThrow();
    assertThat(estudiante.getHistoriaAcademica().getCoeficiente()).isEqualTo(89.78);
  }

  @Test
  void fallaSiMateriaNoExiste() {
    EstudianteRepository estudianteRepo = new EstudianteRepositoryInMemory();
    MateriaRepository materiaRepo = new MateriaRepositoryInMemory(); // vacío
    ComisionRepositoryInMemory comisionRepo = new ComisionRepositoryInMemory();

    EstudianteConHistoriaAcademicaSeeder seeder = new EstudianteConHistoriaAcademicaSeeder(
      estudianteRepo, materiaRepo, comisionRepo, new ClasspathResourceLoader());

    Exception ex = assertThrows(RuntimeException.class,
      () -> seeder.cargarEstudiante("csv/historiaAcademica_materia_inexistente.csv"));

    assertThat(ex.getMessage()).contains("No se encontró la materia");
  }

  @Test
  void cargaCorrectamenteMateriasSeparadasPorComaSinEspacios() throws Exception {
    MateriaRepository materiaRepo = new MateriaRepositoryInMemory();
    materiaRepo.save(Materia.builder().codigo("1041").nombre("Test").build());
    materiaRepo.save(Materia.builder().codigo("1046").nombre("Test").build());

    ComisionRepositoryInMemory comisionRepo = new ComisionRepositoryInMemory();
    comisionRepo.save(
      Comision.builder().codigo("1041").materia(materiaRepo.findByCodigo("1041").orElseThrow())
              .build());
    comisionRepo.save(
      Comision.builder().codigo("1046").materia(materiaRepo.findByCodigo("1046").orElseThrow())
              .build());

    EstudianteRepository estudianteRepo = new EstudianteRepositoryInMemory();
    EstudianteConHistoriaAcademicaSeeder seeder = new EstudianteConHistoriaAcademicaSeeder(
      estudianteRepo, materiaRepo, comisionRepo, new ClasspathResourceLoader());

    seeder.cargarEstudiante("csv/historiaAcademica_comas_sin_espacios.csv");

    assertThat(estudianteRepo.findAll()).hasSize(1);
  }
}
