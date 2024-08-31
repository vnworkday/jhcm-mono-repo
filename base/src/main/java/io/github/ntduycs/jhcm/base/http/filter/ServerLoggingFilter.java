package io.github.ntduycs.jhcm.base.http.filter;

import static io.github.ntduycs.jhcm.base.logging.LoggingConstant.*;

import io.github.ntduycs.jhcm.base.http.HttpConstant;
import io.github.ntduycs.jhcm.base.http.HttpProperties;
import io.github.ntduycs.jhcm.base.http.interceptor.ServerRequestLoggingInterceptor;
import io.github.ntduycs.jhcm.base.http.interceptor.ServerResponseLoggingInterceptor;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebExchangeDecorator;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class ServerLoggingFilter implements WebFilter {
  private final HttpProperties properties;

  @Override
  @NonNull public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
    var startTime = Instant.now();

    setMDCRequest(exchange);

    exchange
        .getResponse()
        .getHeaders()
        .add(HttpConstant.HTTP_HEADER_REQUEST_ID, MDC.get(REQUEST_ID));

    if (!properties.getLogging().getRequest().isEnabled()
        && !properties.getLogging().getResponse().isEnabled()) {
      return chain.filter(exchange);
    }

    return chain
        .filter(new LoggingDecorator(exchange, properties, startTime))
        .doFinally(signal -> MDC.clear());
  }

  private void setMDCRequest(ServerWebExchange exchange) {
    var requestId = exchange.getRequest().getId();
    MDC.put(REQUEST_ID, Optional.of(requestId).orElse(UUID.randomUUID().toString()));

    var correlationId =
        exchange.getRequest().getHeaders().getFirst(HttpConstant.HTTP_HEADER_CORRELATION_ID);
    MDC.put(
        CORRELATION_ID,
        Optional.ofNullable(correlationId)
            .orElse(ObjectUtils.getIdentityHexString(exchange.getRequest())));

    var method = exchange.getRequest().getMethod();
    MDC.put(METHOD, method.name());

    var path = exchange.getRequest().getURI().getPath();
    MDC.put(PATH, path);
  }

  static class LoggingDecorator extends ServerWebExchangeDecorator {
    private final ServerRequestLoggingInterceptor loggableRequest;
    private final ServerResponseLoggingInterceptor loggableResponse;

    @Override
    @NonNull public ServerHttpRequest getRequest() {
      return loggableRequest;
    }

    @Override
    @NonNull public ServerHttpResponse getResponse() {
      return loggableResponse;
    }

    protected LoggingDecorator(
        ServerWebExchange delegate, HttpProperties properties, Instant startTime) {
      super(delegate);
      this.loggableRequest =
          new ServerRequestLoggingInterceptor(
              delegate.getRequest(), properties.getLogging().getRequest());
      this.loggableResponse =
          new ServerResponseLoggingInterceptor(
              delegate.getResponse(), properties.getLogging().getResponse(), startTime);
    }
  }
}
