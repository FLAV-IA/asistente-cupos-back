package com.edu.asistenteCupos.seeder;

import com.edu.asistenteCupos.Utils.ClasspathResourceLoader;
import com.edu.asistenteCupos.domain.Comision;
import com.edu.asistenteCupos.domain.Materia;
import com.edu.asistenteCupos.repository.ComisionRepository;
import com.edu.asistenteCupos.repository.MateriaRepository;
import com.edu.asistenteCupos.repository.impl.memory.ComisionRepositoryInMemory;
import com.edu.asistenteCupos.repository.impl.memory.MateriaRepositoryInMemory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ComisionSeederTest {
  @Test
  void cargaComisionesDesdeCSV() throws Exception {
    ComisionRepository comisionRepo = new ComisionRepositoryInMemory();
    MateriaRepository materiaRepo = new MateriaRepositoryInMemory();
    ClasspathResourceLoader loader = new ClasspathResourceLoader();
    materiaRepo.save(Materia.builder().codigo("TEST101").nombre("Intro Test").build());
    materiaRepo.save(Materia.builder().codigo("TEST102").nombre("Avanzado Test").build());

    ComisionSeeder seeder = new ComisionSeeder(comisionRepo, materiaRepo, loader);
    seeder.cargarComisiones("comisiones_test.csv");

    List<Comision> comisiones = comisionRepo.findAll();
    assertThat(comisiones).hasSize(2);
    Comision comision = comisionRepo.findById("TEST101-COM1").orElseThrow();
    assertThat(comision.getHorario()).isEqualTo("Martes 10:00 a 12:00");
    assertThat(comision.getCupo()).isEqualTo(30);
    assertThat(comision.getMateria()).isNotNull();
    assertThat(comision.getMateria().getCodigo()).isEqualTo("TEST101");
  }
}
