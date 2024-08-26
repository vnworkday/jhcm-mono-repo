package io.github.ntduycs.jhcm.base.http;

import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class HttpConstant {
  public static final String HTTP_HEADER_REQUEST_ID = "X-Request-ID";
  public static final String HTTP_HEADER_CORRELATION_ID = "X-Correlation-ID";

  public static final int HTTP_REQUEST_DEFAULT_OFFSET = 0;
  public static final int HTTP_REQUEST_DEFAULT_SIZE = 10;
  public static final String HTTP_REQUEST_DEFAULT_SORT = "updated_at";
  public static final String HTTP_REQUEST_DEFAULT_ORDER = "desc";
}
