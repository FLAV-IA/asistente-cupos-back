package com.edu.asistente_cupos.repository.spring;

import com.edu.asistente_cupos.domain.Comision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface ComisionJpaRepository extends JpaRepository<Comision, String> {
  List<Comision> findByCodigoIn(Collection<String> codigos);

  @Query("SELECT c.cupo FROM Comision c WHERE c.codigo = :codigo")
  int findCupoByCodigo(@Param("codigo") String codigo);

}
