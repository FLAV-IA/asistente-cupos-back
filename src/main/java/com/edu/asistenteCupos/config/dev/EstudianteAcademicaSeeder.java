package com.edu.asistenteCupos.config.dev;

import com.edu.asistenteCupos.Utils.ClasspathResourceLoader;
import com.edu.asistenteCupos.domain.Estudiante;
import com.edu.asistenteCupos.domain.HistoriaAcademica;
import com.edu.asistenteCupos.domain.Materia;
import com.edu.asistenteCupos.repository.EstudianteRepository;
import com.edu.asistenteCupos.repository.MateriaRepository;
import lombok.RequiredArgsConstructor;
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
public class EstudianteAcademicaSeeder {
    private final EstudianteRepository alumnoRepository;
    private final MateriaRepository materiaRepository;
    private final ClasspathResourceLoader resourceLoader;
    String nombreCsv = "csv/historiaAcademica.csv";
    public void cargarEstudiante(String nombreArchivo) throws Exception {
        List<String[]> rows = resourceLoader.leerCSV(nombreArchivo, "\\|");
        for (String[] row : rows.stream().skip(1).toList()) {
            Estudiante estudiante = obtenerEstudiante(row);
            alumnoRepository.save(estudiante);
        }
    }

    private Estudiante obtenerEstudiante(String[] row) {
        String legajo = row[0].trim();
        String nombre = row[1].trim();
        String mail = row[2].trim();
        int insc3 = Integer.parseInt(row[3].trim());
        int aprobUlt = Integer.parseInt(row[4].trim());
        int inscTot = Integer.parseInt(row[5].trim());
        int aprobTot = Integer.parseInt(row[6].trim());
        int restantes = Integer.parseInt(row[7].trim());
        Boolean cumpleCorrelatividad = Boolean.valueOf(row[8]);
        String[] anotadas = row[9].trim().split(",");

        Set<Materia> materiasAnotadas = Arrays.stream(anotadas).map(materia->this.materiaRepository.findByCodigo(materia).orElseThrow(()->new RuntimeException("No se encontro la materia con el codigo: "+materia)))
                .collect(Collectors.toSet());
        HistoriaAcademica historiaAcademica = HistoriaAcademica.builder().cantInscripciones3CursadasPrevias(insc3)
                .cantAprobadas3CursadasPrevias(aprobUlt)
                .cantInscripcionesHistoricas(inscTot)
                .cantMateriasAprobadasHistoricas(aprobTot)
                .cantMateriasRestantes(restantes)
                .cumpleCorrelatividad(cumpleCorrelatividad)
                .materiasActuales(materiasAnotadas)
                .build();
        Estudiante estudiante = Estudiante.builder().legajo(legajo)
                                .nombre(nombre).mail(mail).build();
        historiaAcademica.setEstudiante(estudiante);
        estudiante.setHistoriaAcademica(historiaAcademica);
        return estudiante;
    }

    @Bean
    @Order(3)
    @Profile("dev")
    CommandLineRunner runHistoriaAcademicaSeeder() {
        return args -> cargarEstudiante(nombreCsv);
    }
}
