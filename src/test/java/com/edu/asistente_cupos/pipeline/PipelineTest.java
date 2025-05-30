package com.edu.asistente_cupos.pipeline;


import com.edu.asistente_cupos.config.MockConfig;
import com.edu.asistente_cupos.domain.peticion.PeticionInscripcion;
import com.edu.asistente_cupos.domain.priorizacion.PeticionPorMateriaPriorizada;
import com.edu.asistente_cupos.domain.sugerencia.SugerenciaInscripcion;
import com.edu.asistente_cupos.service.llm.LlmClient;
import com.edu.asistente_cupos.testutils.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
@Import(MockConfig.class)
class PipelineTest {
  @Container
  protected static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
    .withDatabaseName("asistente").withUsername("root").withPassword("test");
  @Autowired
  private LlmClient llmClient;
  @Autowired
  private Paso<List<PeticionInscripcion>, List<PeticionInscripcion>> filtroPaso;
  @Autowired
  private Paso<List<PeticionInscripcion>, List<PeticionPorMateriaPriorizada>> priorizacionPaso;
  @Autowired
  private Paso<List<PeticionPorMateriaPriorizada>, List<SugerenciaInscripcion>> asignacionPaso;
  @Autowired
  private Paso<List<SugerenciaInscripcion>, List<SugerenciaInscripcion>> traduccionYConversorPaso;

  @DynamicPropertySource
  static void registrarPropiedades(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", mysql::getJdbcUrl);
    registry.add("spring.datasource.username", mysql::getUsername);
    registry.add("spring.datasource.password", mysql::getPassword);
    registry.add("spring.datasource.driver-class-name", mysql::getDriverClassName);
  }

  @Test
  void ejecutaTodoElPipelineConBeansRealesYMockDeLlm() {
    var peticiones = List.of(TestDataFactory.crearPeticionInscripcionParaPipeline());

    var filtradas = filtroPaso.ejecutar(peticiones);

    var priorizadas = priorizacionPaso.ejecutar(filtradas);

    var sugerencias = asignacionPaso.ejecutar(priorizadas);

    var resultado = traduccionYConversorPaso.ejecutar(sugerencias);

    assertThat(resultado).isNotEmpty();
  }
}
