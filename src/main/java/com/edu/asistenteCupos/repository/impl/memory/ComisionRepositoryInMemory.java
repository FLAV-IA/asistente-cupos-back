package com.edu.asistenteCupos.repository.impl.memory;

import com.edu.asistenteCupos.domain.Comision;
import com.edu.asistenteCupos.repository.ComisionRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ComisionRepositoryInMemory implements ComisionRepository {
  private final Map<String, Comision> data = new HashMap<>();

  @Override
  public Comision save(Comision comision) {
    data.put(comision.getCodigo(), comision);
    return comision;
  }

  @Override
  public List<Comision> findAll() {
    return new ArrayList<>(data.values());
  }

  @Override
  public Optional<Comision> findById(String id) {
    return Optional.ofNullable(data.get(id));
  }

  @Override
  public List<Comision> findByCodigoIn(Set<String> codigos) {
    return codigos.stream().map(data::get).filter(Objects::nonNull).collect(Collectors.toList());
  }

  @Override
  public int findCupoByCodigo(String codigo) {
    Comision comision = data.get(codigo);
    if (comision != null) {
      return comision.getCupo();
    }
    return 0;
  }
}
