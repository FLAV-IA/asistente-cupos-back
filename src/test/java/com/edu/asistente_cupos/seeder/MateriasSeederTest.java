package com.edu.asistente_cupos.seeder;

import com.edu.asistente_cupos.Utils.ClasspathResourceLoader;
import com.edu.asistente_cupos.domain.Materia;
import com.edu.asistente_cupos.repository.MateriaRepository;
import com.edu.asistente_cupos.repository.impl.memory.MateriaRepositoryInMemory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MateriasSeederTest {
  MateriaRepository materiaRepository;
  ClasspathResourceLoader resourceLoader;
  MateriasSeeder materiasSeeder;

  @BeforeEach
  void setUp() {
    materiaRepository = new MateriaRepositoryInMemory();
    resourceLoader = mock(ClasspathResourceLoader.class);
    materiasSeeder = new MateriasSeeder(materiaRepository, resourceLoader);
  }

  @Test
  void cargaMateriasYCorrelativasCorrectamente() throws Exception {
    List<String[]> data = List.of(new String[]{"nombre", "codigo", "correlativas"},
      new String[]{"Álgebra", "1001", ""}, new String[]{"Análisis I", "1002", "1001"},
      new String[]{"Física I", "1003", "1001,1002"});
    when(resourceLoader.leerCSV("csv/materias.csv", "\\|")).thenReturn(data);


    materiasSeeder.cargarMaterias("csv/materias.csv");


    Materia analisis = materiaRepository.findByCodigo("1002").orElseThrow();
    assertEquals(1, analisis.getCorrelativas().size());
    assertEquals("1001", analisis.getCorrelativas().get(0).getCodigo());

    Materia fisica = materiaRepository.findByCodigo("1003").orElseThrow();
    assertEquals(2, fisica.getCorrelativas().size());

    List<String> codigos = fisica.getCorrelativas().stream().map(Materia::getCodigo).toList();
    assertTrue(codigos.containsAll(List.of("1001", "1002")));
  }
}
