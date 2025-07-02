package com.edu.asistente_cupos.service;

import com.edu.asistente_cupos.controller.dto.InfoComisionDto;
import com.edu.asistente_cupos.domain.Comision;
import com.edu.asistente_cupos.mapper.ComisionMapper;
import com.edu.asistente_cupos.mapper.EstudianteMapper;
import com.edu.asistente_cupos.repository.ComisionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AsistenteDeComisiones{
    private final ComisionRepository comisionRepository;
    private final ComisionMapper comisionMapper;
    private final EstudianteMapper  estudianteMapper;
    public List<InfoComisionDto> obtenerComisiones() {
        List<Comision> comisiones = comisionRepository.findAll();
        return comisiones.stream().map(
                comision -> {
                    InfoComisionDto infoComisionDto = comisionMapper.toComisionActualizadaDto(comision);
                    infoComisionDto.setEstudiantesInscriptos(
                            estudianteMapper.toDtoList(
                                    comision.estudiantesInscriptos())
                    );
                    return infoComisionDto;
                }
        ).toList();
    }
}
