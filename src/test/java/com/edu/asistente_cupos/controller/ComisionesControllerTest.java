package com.edu.asistente_cupos.controller;


import com.edu.asistente_cupos.controller.dto.InfoComisionDto;
import com.edu.asistente_cupos.excepcion.handler.GlobalExceptionHandler;
import com.edu.asistente_cupos.service.AsistenteDeComisiones;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ComisionesControllerTest {

    private MockMvc mockMvc;
    private AsistenteDeComisiones asistenteDeComisiones;

    @BeforeEach
    void setUp() {
        asistenteDeComisiones = mock(AsistenteDeComisiones.class);

        ComisionesController controller = new ComisionesController(asistenteDeComisiones);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .build();
    }

    @Test
    void obtenerComisiones_devuelveListaDeComisiones() throws Exception {
        InfoComisionDto dto1 = new InfoComisionDto();
        dto1.setCodigo("COM-101");
        dto1.setMateria("Algoritmos");

        InfoComisionDto dto2 = new InfoComisionDto();
        dto2.setCodigo("COM-102");
        dto2.setMateria("Estructuras");

        when(asistenteDeComisiones.obtenerComisiones()).thenReturn(List.of(dto1, dto2));

        mockMvc.perform(get("/comisiones/obtenerComisiones")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].codigo").value("COM-101"))
                .andExpect(jsonPath("$[1].materia").value("Estructuras"));
    }

    @Test
    void obtenerComisiones_retornaListaVacia() throws Exception {
        when(asistenteDeComisiones.obtenerComisiones()).thenReturn(List.of());

        mockMvc.perform(get("/comisiones/obtenerComisiones")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void obtenerComisiones_lanza500SiServicioFalla() throws Exception {
        when(asistenteDeComisiones.obtenerComisiones()).thenThrow(new RuntimeException("fallo inesperado"));

        mockMvc.perform(get("/comisiones/obtenerComisiones"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("fallo inesperado"))
                .andExpect(jsonPath("$.status").value(500));
    }
}
