package io.github.ntduycs.jhcm.base.http;

import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class HttpConstant {
  public static final String HTTP_HEADER_REQUEST_ID = "X-Request-ID";
  public static final String HTTP_HEADER_CORRELATION_ID = "X-Correlation-ID";
  public static final String HTTP_HEADER_REQUEST_METHOD = "X-Request-Method";
  public static final String HTTP_HEADER_REQUEST_PATH = "X-Request-Path";
}
