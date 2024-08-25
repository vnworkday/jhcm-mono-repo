package io.github.ntduycs.jhcm.base.http.exception.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HttpError {
  OK(200, "OK", false),

  // Client errors
  BAD_REQUEST(400, "Bad Request", false),
  UNAUTHENTICATED(401, "Unauthenticated", false),
  PERMISSION_DENIED(403, "Permission Denied", false),
  NOT_FOUND(404, "Not Found", false),
  DEADLINE_EXCEEDED(405, "Deadline Exceeded", true),
  ALREADY_EXISTS(406, "Already Exists", false),
  EXHAUSTED(407, "Too Many Requests", true),
  OUTDATED(408, "Outdated", true),

  // Server errors
  INTERNAL(500, "Internal Server Error", true),
  UNIMPLEMENTED(501, "Not Implemented", false),
  UNAVAILABLE(503, "Service Unavailable", true);

  private final int code;
  private final String title;
  private final boolean retryable;
}
