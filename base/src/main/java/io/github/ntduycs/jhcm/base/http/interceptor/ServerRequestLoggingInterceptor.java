package io.github.ntduycs.jhcm.base.http.interceptor;

import static io.github.ntduycs.jhcm.base.logging.LoggingConstant.REQUEST_ID;

import io.github.ntduycs.jhcm.base.http.HttpProperties;
import io.github.ntduycs.jhcm.base.http.interceptor.model.CaseInsensitiveHeaders;
import io.github.ntduycs.jhcm.base.util.JsonUtils;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.lang.NonNull;
import org.springframework.util.LinkedCaseInsensitiveMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Flux;

@Slf4j
public class ServerRequestLoggingInterceptor extends ServerHttpRequestDecorator {
  private final Flux<DataBuffer> rawBody;

  public ServerRequestLoggingInterceptor(
      ServerHttpRequest delegate, HttpProperties.Logging.Request properties) {
    super(delegate);

    var path = delegate.getURI().getPath();

    if (properties.getIgnoredUris() != null) {
      for (var ignoredUri : properties.getIgnoredUris()) {
        if (path.matches(ignoredUri)) {
          this.rawBody = super.getBody();
          return;
        }
      }
    }

    if (properties.isEnabled()) {
      var method = delegate.getMethod().name();
      var requestId = MDC.get(REQUEST_ID);
      log.info("<== Request: [{}] {} - {}", requestId, method, path);

      if (HttpMethod.GET.equals(delegate.getMethod())) {
        log.info("<== Query: {}", JsonUtils.stringify(flat(delegate.getQueryParams())));
      }

      if (properties.isIncludeHeaders()) {
        var headers = CaseInsensitiveHeaders.from(delegate.getHeaders());
        log.info("<== Headers: {}", JsonUtils.stringify(headers));
      }

      this.rawBody = super.getBody().doOnNext(this::logBody);
    } else {
      this.rawBody = super.getBody();
    }
  }

  @Override
  @NonNull public Flux<DataBuffer> getBody() {
    return rawBody;
  }

  private void logBody(DataBuffer dataBuffer) {
    log.debug("<== Body: {}", dataBuffer.toString(StandardCharsets.UTF_8));
  }

  private LinkedCaseInsensitiveMap<String> flat(MultiValueMap<String, String> rawQuery) {
    var query = new LinkedCaseInsensitiveMap<String>();

    for (var entry : rawQuery.entrySet()) {
      for (var value : entry.getValue()) {
        query.put(
            entry.getKey(),
            query.containsKey(entry.getKey()) ? query.get(entry.getKey()) + "," + value : value);
      }
    }

    return query;
  }
}
