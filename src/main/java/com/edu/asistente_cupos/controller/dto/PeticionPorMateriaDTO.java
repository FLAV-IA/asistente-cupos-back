package com.edu.asistente_cupos.controller.dto;

import java.util.List;

public record PeticionPorMateriaDTO(String nombreMateria, String codigoMateria,
                                    List<String> codigosComisionesSolicitadas,
                                    boolean cumpleCorrelativa) {}
