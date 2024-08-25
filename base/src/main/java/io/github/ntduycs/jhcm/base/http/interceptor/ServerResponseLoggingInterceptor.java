package io.github.ntduycs.jhcm.base.http.interceptor;

import static io.github.ntduycs.jhcm.base.logging.LoggingConstant.REQUEST_ID;

import io.github.ntduycs.jhcm.base.http.HttpProperties;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.slf4j.MDC;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.lang.NonNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class ServerResponseLoggingInterceptor extends ServerHttpResponseDecorator {
  private final StringBuilder responseBody = new StringBuilder();

  public ServerResponseLoggingInterceptor(
      ServerHttpResponse delegate, HttpProperties.Logging.Response properties, Instant startTime) {
    super(delegate);

    delegate.beforeCommit(
        () -> {
          if (properties.isEnabled()) {
            log.info(
                "==> Response: [{}] {} took {}ms",
                MDC.get(REQUEST_ID),
                delegate.getStatusCode(),
                Duration.between(startTime, Instant.now()).toMillis());
            log.debug("==> Body: {}", responseBody.isEmpty() ? "(empty)" : responseBody.toString());
          }

          return Mono.empty();
        });
  }

  @Override
  @NonNull public Mono<Void> writeWith(@NonNull Publisher<? extends DataBuffer> body) {
    return super.writeWith(Flux.from(body).map(this::copyBodyBuffer));
  }

  @Override
  @NonNull public Mono<Void> writeAndFlushWith(
      @NonNull Publisher<? extends Publisher<? extends DataBuffer>> body) {
    return super.writeAndFlushWith(
        Flux.from(body).map(publisher -> Flux.from(publisher).map(this::copyBodyBuffer)));
  }

  private DataBuffer copyBodyBuffer(DataBuffer buffer) {
    responseBody.append(buffer.toString(StandardCharsets.UTF_8));
    return buffer;
  }
}
