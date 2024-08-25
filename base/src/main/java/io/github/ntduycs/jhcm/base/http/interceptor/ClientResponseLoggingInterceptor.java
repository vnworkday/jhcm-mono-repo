package io.github.ntduycs.jhcm.base.http.interceptor;

import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientResponseLoggingInterceptor {
  public void logResponse(FullHttpResponse response) {
    logResponseMetadata(response);
    logResponseBody(response);
  }

  public void logResponse(HttpResponse response) {
    logResponseMetadata(response);
  }

  public void logResponse(HttpContent response) {
    logResponseBody(response);
  }

  private void logResponseMetadata(HttpResponse response) {
    var status = response.status();
    log.debug("<== Response: {}", status);
  }

  public void logResponseBody(HttpContent content) {
    var body = content.content().toString(StandardCharsets.UTF_8);
    log.debug("<== Body: {}", body);
  }
}
