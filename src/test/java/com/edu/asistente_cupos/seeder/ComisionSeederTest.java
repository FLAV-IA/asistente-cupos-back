package com.edu.asistente_cupos.seeder;

import com.edu.asistente_cupos.Utils.ClasspathResourceLoader;
import com.edu.asistente_cupos.domain.Comision;
import com.edu.asistente_cupos.domain.Materia;
import com.edu.asistente_cupos.repository.ComisionRepository;
import com.edu.asistente_cupos.repository.MateriaRepository;
import com.edu.asistente_cupos.repository.impl.memory.ComisionRepositoryInMemory;
import com.edu.asistente_cupos.repository.impl.memory.MateriaRepositoryInMemory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ComisionSeederTest {
  @Test
  void cargaComisionesDesdeCSVConHorariosSimplesYMultiples() throws Exception {
    ComisionRepository comisionRepo = new ComisionRepositoryInMemory();
    MateriaRepository materiaRepo = new MateriaRepositoryInMemory();
    ClasspathResourceLoader loader = new ClasspathResourceLoader();

    materiaRepo.save(Materia.builder().codigo("TEST101").nombre("Intro Test").build());
    materiaRepo.save(Materia.builder().codigo("TEST102").nombre("Avanzado Test").build());
    materiaRepo.save(Materia.builder().codigo("1043").nombre("Matematica Discreta").build());

    ComisionSeeder seeder = new ComisionSeeder(comisionRepo, materiaRepo, loader);
    seeder.cargarComisiones("comisiones_test.csv");

    List<Comision> comisiones = comisionRepo.findAll();
    assertThat(comisiones).hasSize(3);

    Comision c1 = comisionRepo.findById("TEST101-COM1").orElseThrow();
    assertThat(c1.getHorario().toString()).isEqualTo("MARTES 10:00 a 12:00");
    assertThat(c1.getCupo()).isEqualTo(30);
    assertThat(c1.getMateria().getCodigo()).isEqualTo("TEST101");

    Comision c2 = comisionRepo.findById("TEST102-COM1").orElseThrow();
    assertThat(c2.getHorario().toString()).isEqualTo("JUEVES 14:00 a 16:00");
    assertThat(c2.getCupo()).isEqualTo(25);
    assertThat(c2.getMateria().getCodigo()).isEqualTo("TEST102");

    Comision c3 = comisionRepo.findById("1043-1-G14").orElseThrow();
    assertThat(c3.getHorario().toString()).isEqualTo(
      "MIERCOLES 18:30 a 20:59, JUEVES 18:30 a 21:59");
    assertThat(c3.getCupo()).isEqualTo(5);
    assertThat(c3.getMateria().getCodigo()).isEqualTo("1043");
  }
}