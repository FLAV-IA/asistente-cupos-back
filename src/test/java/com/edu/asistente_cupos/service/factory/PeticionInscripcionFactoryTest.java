package com.edu.asistente_cupos.service.factory;

import com.edu.asistente_cupos.controller.dto.HistoriaAcademicaDTO;
import com.edu.asistente_cupos.controller.dto.PeticionInscripcionCsvDTO;
import com.edu.asistente_cupos.controller.dto.PeticionInscripcionDTO;
import com.edu.asistente_cupos.controller.dto.PeticionPorMateriaDTO;
import com.edu.asistente_cupos.domain.Comision;
import com.edu.asistente_cupos.domain.Estudiante;
import com.edu.asistente_cupos.domain.HistoriaAcademica;
import com.edu.asistente_cupos.domain.Materia;
import com.edu.asistente_cupos.domain.horario.HorarioParser;
import com.edu.asistente_cupos.domain.peticion.PeticionInscripcion;
import com.edu.asistente_cupos.domain.peticion.PeticionPorMateria;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class PeticionInscripcionFactoryTest {
  private final PeticionInscripcionFactory factory = new PeticionInscripcionFactory();

  @Test
  void crearDesdeCsvDevuelvePeticionCorrecta() {
    Estudiante estudiante = Estudiante.builder().dni("123").nombre("Juan").build();
    Materia materia = Materia.builder().codigo("MAT1").nombre("Álgebra").build();

    Comision com1 = new Comision("COM1", HorarioParser.parse("LUNES 09:00 a 10:00"), 30, materia);
    Comision com2 = new Comision("COM2", HorarioParser.parse("MARTES 09:00 a 11:00"), 30, materia);

    HistoriaAcademica historia = HistoriaAcademica.builder().coeficiente(7.0)
                                                  .totalHistoricasAprobadas(5)
                                                  .totalInscripcionesHistoricas(6)
                                                  .cursadas(List.of()).build();
    estudiante.setHistoriaAcademica(historia);

    PeticionInscripcionCsvDTO dto = new PeticionInscripcionCsvDTO();
    dto.setDni("123");
    dto.setCodigosComisiones("COM1, COM2");

    Map<String, Comision> disponibles = Map.of("COM1", com1, "COM2", com2);


    PeticionInscripcion peticion = factory.crearDesdeCsv(dto, estudiante, disponibles);


    assertThat(peticion.getEstudiante()).isEqualTo(estudiante);
    assertThat(peticion.getPeticionPorMaterias()).hasSize(1);
    assertThat(peticion.getPeticionPorMaterias().get(0).getComisiones()).containsExactlyInAnyOrder(
      com1, com2);
  }

  @Test
  void crearDesdeCsvIgnoraCodigosInvalidos() {
    Estudiante estudiante = Estudiante.builder().dni("456").nombre("Lucía").build();

    PeticionInscripcionCsvDTO dto = new PeticionInscripcionCsvDTO();
    dto.setDni("456");
    dto.setCodigosComisiones("COMX, , ");

    Map<String, Comision> disponibles = Map.of();


    PeticionInscripcion peticion = factory.crearDesdeCsv(dto, estudiante, disponibles);


    assertThat(peticion.getPeticionPorMaterias()).isEmpty();
  }

  @Test
  void crearDesdeDtoDevuelvePeticionCorrecta() {
    Materia materia = Materia.builder().codigo("MAT2").nombre("Historia").build();
    Comision com1 = new Comision("C1", HorarioParser.parse("MIERCOLES 10:00 a 12:00"), 40, materia);
    Comision com2 = new Comision("C2", HorarioParser.parse("VIERNES 14:00 a 16:00"), 40, materia);

    Estudiante estudiante = Estudiante.builder().dni("789").nombre("Ana").build();
    HistoriaAcademica historia = HistoriaAcademica.builder().coeficiente(8.5)
                                                  .totalHistoricasAprobadas(9)
                                                  .totalInscripcionesHistoricas(10)
                                                  .cursadas(List.of()).build();
    estudiante.setHistoriaAcademica(historia);

    HistoriaAcademicaDTO historiaDTO = HistoriaAcademicaDTO.builder().dni("789")
                                                           .totalInscripcionesHistoricas(10)
                                                           .totalHistoricasAprobadas(9)
                                                           .coeficiente(8.5)
                                                           .cumpleCorrelativas(true)
                                                           .codigosCursadasAnteriores(
                                                             List.of("MAT1"))
                                                           .codigosInscripcionesActuales(
                                                             Set.of("MAT3")).build();

    PeticionPorMateriaDTO materiaDto = new PeticionPorMateriaDTO("Historia", "MAT2",
      List.of("C1", "C2"), true);
    PeticionInscripcionDTO dto = new PeticionInscripcionDTO("Ana", "789", historiaDTO,
      List.of(materiaDto));

    Map<String, Comision> disponibles = Map.of("C1", com1, "C2", com2);


    PeticionInscripcion peticion = factory.crearDesdeDto(dto, estudiante, disponibles, null);


    assertThat(peticion.getEstudiante()).isEqualTo(estudiante);
    List<PeticionPorMateria> peticiones = peticion.getPeticionPorMaterias();
    assertThat(peticiones).hasSize(1);
    assertThat(peticiones.get(0).getComisiones()).containsExactlyInAnyOrder(com1, com2);
  }

  @Test
  void crearDesdeDtoFiltraComisionesInexistentes() {
    Estudiante estudiante = Estudiante.builder().dni("999").nombre("Carlos").build();

    PeticionPorMateriaDTO materiaDto = new PeticionPorMateriaDTO("Física", "MAT9",
      List.of("INEXISTENTE"), true);
    PeticionInscripcionDTO dto = new PeticionInscripcionDTO("Carlos", "999", null,
      List.of(materiaDto));

    Map<String, Comision> disponibles = Map.of();


    PeticionInscripcion peticion = factory.crearDesdeDto(dto, estudiante, disponibles, null);


    assertThat(peticion.getPeticionPorMaterias()).isEmpty();
  }
}
