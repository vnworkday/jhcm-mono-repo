package io.github.ntduycs.jhcm.base.http.exception;

import io.github.ntduycs.jhcm.base.http.exception.model.HttpError;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class HttpErrorMapper {

  public static HttpStatus fromError(HttpError error) {
    return switch (error) {
      case OK -> HttpStatus.OK;
        // Client errors
      case UNAUTHENTICATED -> HttpStatus.UNAUTHORIZED;
      case PERMISSION_DENIED -> HttpStatus.FORBIDDEN;
      case NOT_FOUND -> HttpStatus.NOT_FOUND;
      case ALREADY_EXISTS -> HttpStatus.CONFLICT;
      case OUTDATED -> HttpStatus.PRECONDITION_FAILED;
      case EXHAUSTED -> HttpStatus.TOO_MANY_REQUESTS;
      case DEADLINE_EXCEEDED -> HttpStatus.REQUEST_TIMEOUT;
        // Server errors
      case INTERNAL -> HttpStatus.INTERNAL_SERVER_ERROR;
      case UNIMPLEMENTED -> HttpStatus.NOT_IMPLEMENTED;
      case UNAVAILABLE -> HttpStatus.SERVICE_UNAVAILABLE;
      default -> throw new IllegalArgumentException("Unsupported error: " + error);
    };
  }

  public static HttpError fromHttpStatus(HttpStatusCode status) {
    if (status.is2xxSuccessful()) {
      return HttpError.OK;
    }

    return switch (status.value()) {
      case 401 -> HttpError.UNAUTHENTICATED;
      case 403 -> HttpError.PERMISSION_DENIED;
      case 404 -> HttpError.NOT_FOUND;
      case 408 -> HttpError.DEADLINE_EXCEEDED;
      case 409 -> HttpError.ALREADY_EXISTS;
      case 412 -> HttpError.OUTDATED;
      case 429 -> HttpError.EXHAUSTED;
      case 500 -> HttpError.INTERNAL;
      case 501 -> HttpError.UNIMPLEMENTED;
      case 503 -> HttpError.UNAVAILABLE;
      default -> throw new IllegalArgumentException("Unsupported status: " + status);
    };
  }
}
