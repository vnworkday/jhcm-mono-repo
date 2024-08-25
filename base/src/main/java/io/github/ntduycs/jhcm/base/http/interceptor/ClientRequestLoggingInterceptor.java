package io.github.ntduycs.jhcm.base.http.interceptor;

import io.github.ntduycs.jhcm.base.http.interceptor.model.CaseInsensitiveHeaders;
import io.github.ntduycs.jhcm.base.util.JsonUtils;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.LastHttpContent;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@Slf4j
public class ClientRequestLoggingInterceptor {
  public void logRequest(FullHttpRequest request) {
    logRequestMetadata(request);
    logRequestBody(
        request, request.headers().get(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
  }

  public void logRequest(HttpRequest request) {
    logRequestMetadata(request);
  }

  public void logRequest(FullHttpMessage request) {
    logRequestBody(
        request, request.headers().get(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
  }

  private void logRequestBody(LastHttpContent message, String contentType) {
    var body = message.content().toString(StandardCharsets.UTF_8);

    var isFormUrlEncoded = contentType.contains(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
    if (isFormUrlEncoded) {
      var keyValues = body.split("&");
      var json =
          Arrays.stream(keyValues)
              .map(keyValue -> keyValue.split("="))
              .collect(Collectors.toMap(keyValue -> keyValue[0], keyValue -> keyValue[1]));
      body = JsonUtils.stringify(json);
    }

    log.debug("==> Body: {}", body);
  }

  private void logRequestMetadata(HttpRequest request) {
    var method = request.method();
    var uri = request.uri();
    log.debug("==> Request: {} - {}", method, uri);
    log.debug(
        "==> Headers: {}", JsonUtils.stringify(CaseInsensitiveHeaders.from(request.headers())));
  }
}
