package com.edu.asistenteCupos.seeder;

import com.edu.asistenteCupos.Utils.ClasspathResourceLoader;
import com.edu.asistenteCupos.domain.Comision;
import com.edu.asistenteCupos.domain.Estudiante;
import com.edu.asistenteCupos.domain.Materia;
import com.edu.asistenteCupos.repository.EstudianteRepository;
import com.edu.asistenteCupos.repository.MateriaRepository;
import com.edu.asistenteCupos.repository.impl.memory.ComisionRepositoryInMemory;
import com.edu.asistenteCupos.repository.impl.memory.EstudianteRepositoryInMemory;
import com.edu.asistenteCupos.repository.impl.memory.MateriaRepositoryInMemory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class EstudianteSeederTest {
  @Test
  void cargaAlumnoConHistoriaAcademicaDesdeCSVDeTest() throws Exception {
    MateriaRepository materiaRepositoryInMemory = new MateriaRepositoryInMemory();
    materiaRepositoryInMemory.save(Materia.builder().codigo("646").nombre("Intro Test").build());
    materiaRepositoryInMemory.save(Materia.builder().codigo("487").nombre("Intro Test").build());
    materiaRepositoryInMemory.save(Materia.builder().codigo("1038").nombre("Intro Test").build());
    materiaRepositoryInMemory.save(Materia.builder().codigo("1035").nombre("Intro Test").build());
    materiaRepositoryInMemory.save(Materia.builder().codigo("1041").nombre("Intro Test").build());
    materiaRepositoryInMemory.save(Materia.builder().codigo("1042").nombre("Intro Test").build());
    materiaRepositoryInMemory.save(Materia.builder().codigo("1048").nombre("Intro Test").build());
    materiaRepositoryInMemory.save(
      Materia.builder().codigo("1046").nombre("Avanzado Test").build());

    EstudianteRepository estudianteRepositoryInMemory = new EstudianteRepositoryInMemory();
    ComisionRepositoryInMemory comisionRepositoryInMemory = new ComisionRepositoryInMemory();
    comisionRepositoryInMemory.save(
      Comision.builder().codigo("1046").materia(materiaRepositoryInMemory.findByCodigo("1046").orElseThrow()).build());
    comisionRepositoryInMemory.save(
            Comision.builder().codigo("1041").materia(materiaRepositoryInMemory.findByCodigo("1041").orElseThrow()).build());
    EstudianteConHistoriaAcademicaSeeder estudianteConHistoriaAcademicaSeeder = new EstudianteConHistoriaAcademicaSeeder(
      estudianteRepositoryInMemory, materiaRepositoryInMemory,comisionRepositoryInMemory, new ClasspathResourceLoader());


    estudianteConHistoriaAcademicaSeeder.cargarEstudiante("historiaAcademica_test.csv");


    List<Estudiante> all = estudianteRepositoryInMemory.findAll();
    assertThat(all).hasSize(2);
    Estudiante estudianteBuscado = estudianteRepositoryInMemory.findByDni("100").orElseThrow();
    assertThat(estudianteBuscado.getHistoriaAcademica().getCoeficiente()).isEqualTo(89.78);
  }
}
