package com.edu.asistente_cupos.excepcion.handler;

import com.edu.asistente_cupos.excepcion.config.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Import(TestConfig.class)
class GlobalExceptionHandlerTest {
  @Autowired
  private WebApplicationContext webApplicationContext;

  private MockMvc mockMvc;

  @BeforeEach
  void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
  }

  @Test
  void deberiaManejarExceptionGenericaYRetornarInternalServerError() throws Exception {
    String mensajeError = "Algo salió muy mal y no lo esperábamos.";
    mockMvc.perform(get("/dummy/lanzar-generica").param("mensaje", mensajeError)
                                                 .accept(MediaType.APPLICATION_JSON))
           .andExpect(status().isInternalServerError())
           .andExpect(jsonPath("$.status").value(HttpStatus.INTERNAL_SERVER_ERROR.value()))
           .andExpect(jsonPath("$.error").value(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()))
           .andExpect(jsonPath("$.message").value(mensajeError));
  }

  @Test
  void deberiaManejarIllegalArgumentExceptionYRetornarBadRequest() throws Exception {
    String mensajeError = "Argumento de entrada inválido proporcionado.";
    mockMvc.perform(get("/dummy/lanzar-illegal-argument").param("mensaje", mensajeError))
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
           .andExpect(jsonPath("$.error").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
           .andExpect(jsonPath("$.message").value(mensajeError));
  }

  @Test
  void deberiaManejarMissingServletRequestPartExceptionYRetornarBadRequest() throws Exception {
    mockMvc.perform(get("/dummy/lanzar-missing-request-part").accept(MediaType.APPLICATION_JSON))
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
           .andExpect(jsonPath("$.error").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
           .andExpect(jsonPath("$.message").value(
             "Required part 'Archivo requerido no presente' is not present."));
  }

  @Test
  void deberiaManejarMissingServletRequestParameterExceptionYRetornarBadRequest() throws Exception {
    mockMvc.perform(get("/dummy/lanzar-missing-param").accept(MediaType.APPLICATION_JSON))
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
           .andExpect(jsonPath("$.error").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
           .andExpect(jsonPath("$.message").value(
             "Required request parameter 'param' for method parameter type String is not present"));
  }

  @Test
  void deberiaManejarConfiguracionDeAsignacionInvalidaExceptionYRetornarInternalServerError() throws Exception {
    String mensajeError = "La configuración de asignación es inválida.";
    mockMvc.perform(get("/dummy/lanzar-configuracion-invalida").param("mensaje", mensajeError)
                                                               .accept(MediaType.APPLICATION_JSON))
           .andExpect(status().isInternalServerError())
           .andExpect(jsonPath("$.status").value(HttpStatus.INTERNAL_SERVER_ERROR.value()))
           .andExpect(jsonPath("$.error").value(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()))
           .andExpect(jsonPath("$.message").value(mensajeError));
  }

  @Test
  void deberiaManejarArchivoCsvInvalidoExceptionYRetornarBadRequest() throws Exception {
    String mensajeError = "El archivo CSV proporcionado tiene un formato inválido.";
    mockMvc.perform(get("/dummy/lanzar-archivo-csv-invalido").param("mensaje", mensajeError))
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
           .andExpect(jsonPath("$.error").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
           .andExpect(jsonPath("$.message").value(mensajeError));
  }

  @Test
  void deberiaManejarComisionesDeDistintaMateriaExceptionYRetornarBadRequest() throws Exception {
    String mensajeError = "Las comisiones seleccionadas pertenecen a distintas materias.";
    mockMvc.perform(get("/dummy/lanzar-comisiones-distinta-materia").param("mensaje", mensajeError))
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
           .andExpect(jsonPath("$.error").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
           .andExpect(jsonPath("$.message").value(mensajeError));
  }

  @Test
  void deberiaManejarEstudianteNoEncontradoExceptionYRetornarNotFound() throws Exception {
    String mensajeError = "Estudiante con dni 1234 no encontrado";
    mockMvc
      .perform(get("/dummy/lanzar-estudiante-no-encontrado").accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isNotFound())
      .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
      .andExpect(jsonPath("$.error").value(HttpStatus.NOT_FOUND.getReasonPhrase()))
      .andExpect(jsonPath("$.message").value(mensajeError));
  }

  @Test
  void deberiaManejarHorarioParseExceptionYRetornarBadRequest() throws Exception {
    String mensajeError = "Error al parsear el formato del horario.";
    mockMvc.perform(get("/dummy/lanzar-horario-parse").param("mensaje", mensajeError))
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
           .andExpect(jsonPath("$.error").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
           .andExpect(jsonPath("$.message").value(mensajeError));
  }

  @Test
  void deberiaManejarNoSeEspecificaronComisionesExceptionYRetornarBadRequest() throws Exception {
    String mensajeError = "Se requiere especificar al menos una comisión.";
    mockMvc.perform(get("/dummy/lanzar-no-comisiones-especificadas").param("mensaje", mensajeError))
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
           .andExpect(jsonPath("$.error").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
           .andExpect(jsonPath("$.message").value(mensajeError));
  }
}