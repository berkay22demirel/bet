package com.berkaydemirel.shared.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author berkaydemirel
 */
@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ServiceException.class)
  public ResponseEntity<ErrorDTO> handleServiceException(ServiceException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                         .body(new ErrorDTO(exception.getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
    ObjectError error = exception.getBindingResult().getAllErrors().getFirst();
    String errorMessage = ((FieldError) error).getField() + " " + error.getDefaultMessage();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                         .body(new ErrorDTO(errorMessage));
  }

  @ExceptionHandler(MissingRequestHeaderException.class)
  public ResponseEntity<Object> handleMissingRequestHeaderException(MissingRequestHeaderException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                         .body(new ErrorDTO(exception.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorDTO> handleException(Exception exception) {
    exception.printStackTrace();
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                         .body(new ErrorDTO("An unexpected error occurred! Please try again."));
  }
}