package com.edu.asistenteCupos.repository.impl.memory;

import com.edu.asistenteCupos.domain.HistoriaAcademica;
import com.edu.asistenteCupos.repository.HistoriaAcademicaRepository;

import java.util.*;

public class HistoriaAcademicarRepositoryInMemory implements HistoriaAcademicaRepository {
    private final Map<String, HistoriaAcademica> data = new HashMap<>();

    @Override
    public HistoriaAcademica save(HistoriaAcademica historia) {
        data.put(historia.getEstudiante().getDni(), historia);
        return historia;
    }


    @Override
    public List<HistoriaAcademica> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Optional<HistoriaAcademica> findByIdHistoriaAcademica(Long idHistoriaAcademica) {
        return data.get(idHistoriaAcademica) != null ? Optional.of(data.get(idHistoriaAcademica)) : Optional.empty();
    }
}

