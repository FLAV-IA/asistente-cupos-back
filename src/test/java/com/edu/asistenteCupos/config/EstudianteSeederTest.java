package com.edu.asistenteCupos.config;

import com.edu.asistenteCupos.Utils.ClasspathResourceLoader;
import com.edu.asistenteCupos.config.dev.EstudianteAcademicaSeeder;
import com.edu.asistenteCupos.domain.Estudiante;
import com.edu.asistenteCupos.domain.HistoriaAcademica;
import com.edu.asistenteCupos.domain.Materia;
import com.edu.asistenteCupos.repository.EstudianteRepository;
import com.edu.asistenteCupos.repository.MateriaRepository;
import com.edu.asistenteCupos.repository.impl.memory.EstudianteRepositoryInMemory;
import com.edu.asistenteCupos.repository.impl.memory.MateriaRepositoryInMemory;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class EstudianteSeederTest {

    @Test
    void cargaAlumnoConHistoriaAcademicaDesdeCSVDeTest() throws Exception {
        MateriaRepository materiaRepositoryInMemory = new MateriaRepositoryInMemory();
        EstudianteRepository estudianteRepositoryInMemory = new EstudianteRepositoryInMemory();
        ClasspathResourceLoader loader = new ClasspathResourceLoader();
        materiaRepositoryInMemory.save(Materia.builder().codigo("80005").nombre("Intro Test").build());
        materiaRepositoryInMemory.save(Materia.builder().codigo("80000").nombre("Avanzado Test").build());
        Set<Materia> materiasCorrelativas =Set.of(
                Materia.builder().codigo("80005").build(),
                Materia.builder().codigo("80000").build()
        );
        HistoriaAcademica historiaAcedemica1 = HistoriaAcademica.builder().cantInscripciones3CursadasPrevias(2)
                .cantAprobadas3CursadasPrevias(1).cantInscripcionesHistoricas(6).cantMateriasAprobadasHistoricas(3).cantMateriasRestantes(5).cumpleCorrelatividad(true).materiasActuales(materiasCorrelativas).build();
        HistoriaAcademica historiaAcedemica2 = HistoriaAcademica.builder().cantInscripciones3CursadasPrevias(2)
                .cantAprobadas3CursadasPrevias(1).cantInscripcionesHistoricas(6).cantMateriasAprobadasHistoricas(3).cantMateriasRestantes(5).cumpleCorrelatividad(true).materiasActuales(materiasCorrelativas).build();

        Estudiante estudiante1 = Estudiante.builder().legajo("101").historiaAcademica(historiaAcedemica1).build();
        Estudiante estudiante2 = Estudiante.builder().legajo("100").historiaAcademica(historiaAcedemica2).build();

        estudianteRepositoryInMemory.save(estudiante1);
        estudianteRepositoryInMemory.save(estudiante2);

        EstudianteAcademicaSeeder estudianteAcademicaSeeder = new EstudianteAcademicaSeeder(estudianteRepositoryInMemory, materiaRepositoryInMemory, loader);
        estudianteAcademicaSeeder.cargarEstudiante("historiaAcademica_test.csv");

        List<Estudiante> all = estudianteRepositoryInMemory.findAll();
        assertThat(all).hasSize(2);
        Estudiante estudianteBuscado = estudianteRepositoryInMemory.findByCodigo("100").orElseThrow();
        assertThat(estudianteBuscado.getHistoriaAcademica().getCantAprobadas3CursadasPrevias()).isEqualTo(1);

    }



}
