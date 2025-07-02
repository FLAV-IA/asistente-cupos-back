package com.edu.asistente_cupos.service.asignacion;

import com.edu.asistente_cupos.controller.dto.InfoComisionDto;
import com.edu.asistente_cupos.domain.Comision;
import com.edu.asistente_cupos.domain.Estudiante;
import com.edu.asistente_cupos.mapper.ComisionMapper;
import com.edu.asistente_cupos.mapper.EstudianteMapper;
import com.edu.asistente_cupos.repository.ComisionRepository;
import com.edu.asistente_cupos.service.AsistenteDeComisiones;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AsistenteDeComisionTest {

    private ComisionRepository comisionRepository;
    private ComisionMapper comisionMapper;
    private EstudianteMapper estudianteMapper;

    private AsistenteDeComisiones asistenteDeComisiones;

    @BeforeEach
    void setUp() {
        comisionRepository = mock(ComisionRepository.class);
        comisionMapper = mock(ComisionMapper.class);
        estudianteMapper = mock(EstudianteMapper.class);
        asistenteDeComisiones = new AsistenteDeComisiones(comisionRepository, comisionMapper, estudianteMapper);
    }

    @Test
    void obtenerComisiones_devuelveListaDeDtosMapeados() {
        Comision comisionMock = mock(Comision.class);
        Estudiante estudianteMock = mock(Estudiante.class);
        InfoComisionDto dtoEsperado = new InfoComisionDto();

        when(comisionRepository.findAll()).thenReturn(List.of(comisionMock));
        when(comisionMapper.toComisionActualizadaDto(comisionMock)).thenReturn(dtoEsperado);
        when(comisionMock.estudiantesInscriptos()).thenReturn(List.of(estudianteMock));
        when(estudianteMapper.toDtoList(List.of(estudianteMock))).thenReturn(List.of());

        List<InfoComisionDto> resultado = asistenteDeComisiones.obtenerComisiones();

        assertEquals(1, resultado.size());
        assertEquals(dtoEsperado, resultado.get(0));
        verify(comisionRepository).findAll();
        verify(comisionMapper).toComisionActualizadaDto(comisionMock);
        verify(estudianteMapper).toDtoList(List.of(estudianteMock));
    }

    @Test
    void obtenerComisiones_retornaListaVaciaSiNoHayComisiones() {
        when(comisionRepository.findAll()).thenReturn(List.of());

        List<InfoComisionDto> resultado = asistenteDeComisiones.obtenerComisiones();

        assertTrue(resultado.isEmpty());
        verify(comisionRepository).findAll();
        verifyNoInteractions(comisionMapper, estudianteMapper);
    }

    @Test
    void obtenerComisiones_mapeaCorrectamenteConMultiplesComisiones() {
        Comision comision1 = mock(Comision.class);
        Comision comision2 = mock(Comision.class);

        Estudiante estudiante1 = mock(Estudiante.class);
        Estudiante estudiante2 = mock(Estudiante.class);

        InfoComisionDto dto1 = new InfoComisionDto();
        InfoComisionDto dto2 = new InfoComisionDto();

        when(comisionRepository.findAll()).thenReturn(List.of(comision1, comision2));

        when(comision1.estudiantesInscriptos()).thenReturn(List.of(estudiante1));
        when(comision2.estudiantesInscriptos()).thenReturn(List.of(estudiante2));

        when(comisionMapper.toComisionActualizadaDto(comision1)).thenReturn(dto1);
        when(comisionMapper.toComisionActualizadaDto(comision2)).thenReturn(dto2);

        when(estudianteMapper.toDtoList(List.of(estudiante1))).thenReturn(List.of());
        when(estudianteMapper.toDtoList(List.of(estudiante2))).thenReturn(List.of());

        List<InfoComisionDto> resultado = asistenteDeComisiones.obtenerComisiones();

        assertEquals(2, resultado.size());
        assertTrue(resultado.contains(dto1));
        assertTrue(resultado.contains(dto2));
    }

    @Test
    void obtenerComisiones_lanzaExcepcionSiFallaRepositorio() {
        when(comisionRepository.findAll()).thenThrow(new RuntimeException("Error en base de datos"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> asistenteDeComisiones.obtenerComisiones());

        assertEquals("Error en base de datos", ex.getMessage());
    }
}
