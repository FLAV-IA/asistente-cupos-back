package com.edu.asistente_cupos.controller;

import com.edu.asistente_cupos.assembler.EnsambladorDeSugerenciasAceptadas;
import com.edu.asistente_cupos.controller.dto.SugerenciaInscripcionDTO;
import com.edu.asistente_cupos.domain.Comision;
import com.edu.asistente_cupos.domain.Estudiante;
import com.edu.asistente_cupos.domain.Materia;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaAceptada;
import com.edu.asistente_cupos.mapper.ComisionMapper;
import com.edu.asistente_cupos.service.AsistenteDeAsignacion;
import com.edu.asistente_cupos.service.sugerencia.opta.model.ComisionDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AsistenteAsignacionControllerTest {

    private MockMvc mockMvc;
    private AsistenteDeAsignacion asistenteDeAsignacion;
    private EnsambladorDeSugerenciasAceptadas ensambladorDeSugerenciasAceptadas;
    private ComisionMapper comisionMapper;

    @BeforeEach
    void setUp() {
        asistenteDeAsignacion = mock(AsistenteDeAsignacion.class);
        ensambladorDeSugerenciasAceptadas = mock(EnsambladorDeSugerenciasAceptadas.class);
        comisionMapper = mock(ComisionMapper.class);

        AsignadorController controller = new AsignadorController(asistenteDeAsignacion,
                ensambladorDeSugerenciasAceptadas, comisionMapper);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .build();
    }

    @Test
    void asignarSugerenciasRetornaComisionesDTO() throws Exception {
        SugerenciaInscripcionDTO sugerenciaDTO = SugerenciaInscripcionDTO.builder()
                .dniEstudiante("1234").codigoComision("MAT1-01").build();

        SugerenciaAceptada sugerenciaAceptada = new SugerenciaAceptada(Estudiante.builder().build(), Materia.builder().build(),Comision.builder().build(), "un motivo",10);
        Comision comision = Comision.builder().codigo("MAT1-01").build();
        ComisionDTO comisionDTO = ComisionDTO.builder().codigo("MAT1-01").build();

        when(ensambladorDeSugerenciasAceptadas.ensamblarSugerencias(any()))
                .thenReturn(List.of(sugerenciaAceptada));
        doNothing().when(asistenteDeAsignacion).asignarSugerencias(any());
        when(asistenteDeAsignacion.obtenerComisionesModificadas())
                .thenReturn(Set.of(comision));
        when(comisionMapper.toDtoList(anySet())).thenReturn(Set.of(comisionDTO));

        mockMvc.perform(post("/asignador/asignar-sugerencias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        [{
                            "dniEstudiante": "1234",
                            "codigoComision": "MAT1-01"
                        }]
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].codigo").value("MAT1-01"));
    }

    @Test
    void asignarSugerenciasRetorna500AnteError() throws Exception {
        when(ensambladorDeSugerenciasAceptadas.ensamblarSugerencias(any()))
                .thenThrow(new RuntimeException("Error en procesamiento"));

        mockMvc.perform(post("/asignador/asignar-sugerencias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        [{
                            "dniEstudiante": "1234",
                            "codigoComision": "MAT1-01"
                        }]
                        """))
                .andExpect(status().isInternalServerError());
    }
}
