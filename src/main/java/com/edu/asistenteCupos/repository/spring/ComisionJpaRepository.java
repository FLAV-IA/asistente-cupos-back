package com.edu.asistenteCupos.repository.spring;

import com.edu.asistenteCupos.domain.Comision;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ComisionJpaRepository extends JpaRepository<Comision, String> {
  List<Comision> findByCodigoIn(Collection<String> codigos);
}
