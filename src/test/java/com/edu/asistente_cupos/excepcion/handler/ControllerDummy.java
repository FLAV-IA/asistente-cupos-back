package com.edu.asistente_cupos.excepcion.handler;

import com.edu.asistente_cupos.excepcion.ArchivoCsvInvalidoException;
import com.edu.asistente_cupos.excepcion.ComisionesDeDistintaMateriaException;
import com.edu.asistente_cupos.excepcion.EstudianteNoEncontradoException;
import com.edu.asistente_cupos.excepcion.HorarioParseException;
import com.edu.asistente_cupos.excepcion.NoSeEspecificaronComisionesException;
import com.edu.asistente_cupos.excepcion.opta.ConfiguracionDeGeneracionDeSugerenciaInvalidaException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@RestController
@RequestMapping("/dummy")
public class ControllerDummy {
  @GetMapping("/lanzar-generica")
  public void lanzarExcepcionGenerica(@RequestParam String mensaje) {
    throw new RuntimeException(mensaje);
  }

  @GetMapping("/lanzar-illegal-argument")
  public void lanzarIllegalArgumentException(@RequestParam String mensaje) {
    throw new IllegalArgumentException(mensaje);
  }

  @GetMapping("/lanzar-missing-request-part")
  public void lanzarMissingServletRequestPartException() throws MissingServletRequestPartException {
    throw new MissingServletRequestPartException("Archivo requerido no presente");
  }

  @GetMapping("/lanzar-missing-param")
  public void lanzarMissingServletRequestParameterException() throws MissingServletRequestParameterException {
    throw new MissingServletRequestParameterException("param", "String");
  }

  @GetMapping("/lanzar-configuracion-invalida")
  public void lanzarConfiguracionDeAsignacionInvalidaException(@RequestParam String mensaje) {
    throw new ConfiguracionDeGeneracionDeSugerenciaInvalidaException(mensaje);
  }

  @GetMapping("/lanzar-archivo-csv-invalido")
  public void lanzarArchivoCsvInvalidoException(@RequestParam String mensaje) {
    throw new ArchivoCsvInvalidoException(mensaje, new Throwable("Causa subyacente"));
  }

  @GetMapping("/lanzar-comisiones-distinta-materia")
  public void lanzarComisionesDeDistintaMateriaException(@RequestParam String mensaje) {
    throw new ComisionesDeDistintaMateriaException(mensaje);
  }

  @GetMapping("/lanzar-estudiante-no-encontrado")
  public void lanzarEstudianteNoEncontradoException() {
    throw new EstudianteNoEncontradoException("Estudiante con dni 1234 no encontrado");
  }

  @GetMapping("/lanzar-horario-parse")
  public void lanzarHorarioParseException(@RequestParam String mensaje) {
    throw new HorarioParseException(mensaje);
  }

  @GetMapping("/lanzar-no-comisiones-especificadas")
  public void lanzarNoSeEspecificaronComisionesException(@RequestParam String mensaje) {
    throw new NoSeEspecificaronComisionesException(mensaje);
  }
}