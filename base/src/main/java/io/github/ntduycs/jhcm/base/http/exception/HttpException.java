package io.github.ntduycs.jhcm.base.http.exception;

import io.github.ntduycs.jhcm.base.http.exception.model.HttpError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class HttpException extends RuntimeException {
  private final transient HttpError error;

  public HttpException(HttpError error, String message) {
    super(message);
    this.error = error;
  }
}
