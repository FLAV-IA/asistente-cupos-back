package com.edu.asistente_cupos.excepcion.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorDTO> handleException(Exception ex) {
    HttpStatus status = resolveStatus(ex);
    log.error("Unhandled exception", ex);
    ErrorDTO body = new ErrorDTO(status.value(), status.getReasonPhrase(),
      ex.getMessage());
    return new ResponseEntity<>(body, status);
  }

  private HttpStatus resolveStatus(Exception ex) {
    ResponseStatus annotation = AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class);
    if (annotation != null) {
      return annotation.value();
    }
    if (ex instanceof IllegalArgumentException ||
      ex instanceof MissingServletRequestPartException ||
      ex instanceof MissingServletRequestParameterException) {
      return HttpStatus.BAD_REQUEST;
    }
    return HttpStatus.INTERNAL_SERVER_ERROR;
  }
}
