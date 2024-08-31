package io.github.ntduycs.jhcm.base.http;

import io.github.ntduycs.jhcm.base.http.exception.HttpErrorMapper;
import io.github.ntduycs.jhcm.base.http.exception.HttpException;
import io.github.ntduycs.jhcm.base.http.exception.model.ErrorResponse;
import io.github.ntduycs.jhcm.base.http.exception.model.HttpError;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.resource.NoResourceFoundException;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class HttpExceptionHandler extends ResponseEntityExceptionHandler {
  @Override
  @NonNull protected Mono<ResponseEntity<Object>> handleExceptionInternal(
      @NonNull Exception ex,
      @Nullable Object body,
      @Nullable HttpHeaders headers,
      @Nullable HttpStatusCode status,
      @Nullable ServerWebExchange exchange) {
    logError(ex);

    return Mono.just(
        new ResponseEntity<>(
            new ErrorResponse(HttpError.INTERNAL, ex.getMessage()),
            headers,
            Optional.ofNullable(status).orElse(HttpStatus.INTERNAL_SERVER_ERROR)));
  }

  @Override
  @NonNull protected Mono<ResponseEntity<Object>> handleWebExchangeBindException(
      @NonNull WebExchangeBindException ex,
      @Nullable HttpHeaders headers,
      @Nullable HttpStatusCode status,
      @Nullable ServerWebExchange exchange) {
    logError(ex);

    var response = new ErrorResponse();

    for (var error : ex.getAllErrors()) {
      if (error instanceof FieldError fieldError) {
        response.addError(
            HttpError.BAD_REQUEST,
            String.format("%s: %s", fieldError.getField(), fieldError.getDefaultMessage()));
      } else {
        response.addError(HttpError.BAD_REQUEST, error.getDefaultMessage());
      }
    }

    return Mono.just(
        new ResponseEntity<>(
            response, headers, Optional.ofNullable(status).orElse(HttpStatus.BAD_REQUEST)));
  }

  @Override
  @NonNull protected Mono<ResponseEntity<Object>> handleServerWebInputException(
      @NonNull ServerWebInputException ex,
      @Nullable HttpHeaders headers,
      @Nullable HttpStatusCode status,
      @Nullable ServerWebExchange exchange) {
    logError(ex);

    var detail =
        Optional.ofNullable(ex.getCause()).map(Throwable::getMessage).orElse(ex.getMessage());

    return Mono.just(
        new ResponseEntity<>(
            new ErrorResponse(HttpError.BAD_REQUEST, detail),
            headers,
            Optional.ofNullable(status).orElse(HttpStatus.BAD_REQUEST)));
  }

  @ExceptionHandler(HttpException.class)
  public Mono<ResponseEntity<ErrorResponse>> handleHttpException(HttpException ex) {
    logError(ex);

    var response = new ErrorResponse(ex.getError(), ex.getMessage());
    var status = HttpErrorMapper.fromError(ex.getError());

    return Mono.just(new ResponseEntity<>(response, status));
  }

  @ExceptionHandler(NoResourceFoundException.class)
  public Mono<ResponseEntity<ErrorResponse>> handleNoResourceFoundException(
      NoResourceFoundException ex) {
    logError(ex);

    return Mono.just(
        new ResponseEntity<>(
            new ErrorResponse(HttpError.NOT_FOUND, ex.getMessage()),
            HttpErrorMapper.fromError(HttpError.NOT_FOUND)));
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public Mono<ResponseEntity<ErrorResponse>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
    logError(ex);

    return Mono.just(
        new ResponseEntity<>(
            new ErrorResponse(HttpError.ALREADY_EXISTS, getExceptionMessage(ex)),
            HttpErrorMapper.fromError(HttpError.ALREADY_EXISTS)));
  }

  @ExceptionHandler(SQLException.class)
  public Mono<ResponseEntity<ErrorResponse>> handleSQLException(SQLException ex) {
    logError(ex);

    return Mono.just(
        new ResponseEntity<>(
            new ErrorResponse(HttpError.INTERNAL, ex.getMessage()),
            HttpErrorMapper.fromError(HttpError.INTERNAL)));
  }

  private String getExceptionMessage(Exception ex) {
    return Optional.ofNullable(ex.getCause()).map(Throwable::getMessage).orElse(ex.getMessage());
  }

  private void logError(Exception ex) {
    logException(ex.getCause() != null ? ex.getCause() : ex);
  }

  private void logException(Throwable ex) {
    switch (ex.getClass().getSimpleName()) {
      case "TypeMismatchException" -> logTypeMismatchException((TypeMismatchException) ex);
      case "WebExchangeBindException" -> logWebExchangeBindException((WebExchangeBindException) ex);
      case "JdbcSQLIntegrityConstraintViolationException",
              "SQLIntegrityConstraintViolationException" ->
          logSQLIntegrityConstraintViolationException(
              (SQLIntegrityConstraintViolationException) ex);
      case "HttpException" -> logHttpException((HttpException) ex);
      default -> logGeneralException(ex);
    }
  }

  private void logHttpException(HttpException ex) {
    if (ex.getError().getCode() == HttpError.NOT_FOUND.getCode()) {
      log.error("Found HttpException: {}", ex.getMessage());
    } else {
      logGeneralException(ex);
    }
  }

  private void logSQLIntegrityConstraintViolationException(
      SQLIntegrityConstraintViolationException ex) {
    log.error(
        "Found SQLIntegrityConstraintViolationException: {}",
        Optional.ofNullable(ex.getMessage()).orElse("Unknown"));
  }

  private void logTypeMismatchException(TypeMismatchException ex) {
    log.error(
        "Found TypeMismatchException: {}",
        String.format(
            "Type mismatch for property %s, expected type %s, but got %s.",
            ex.getPropertyName(),
            Optional.ofNullable(ex.getRequiredType()).map(Class::getSimpleName).orElse("Unknown"),
            ex.getValue()));
  }

  private void logWebExchangeBindException(WebExchangeBindException ex) {
    log.error(
        "Found WebExchangeBindException: {} field errors, {} global errors",
        ex.getFieldErrorCount(),
        ex.getGlobalErrorCount());
  }

  private void logGeneralException(Throwable ex) {
    var exception = ex.getClass().getSimpleName();
    var message = ex.getMessage();

    if (ex.getCause() != null) {
      exception = ex.getCause().getClass().getSimpleName();
      message = ex.getCause().getMessage();
    }

    log.error("Found {}", String.format("%s: %s", exception, message), ex);
  }
}
