package io.github.ntduycs.jhcm.base.http.interceptor;

import static io.github.ntduycs.jhcm.base.logging.LoggingConstant.*;
import static java.util.Objects.requireNonNullElse;

import io.github.ntduycs.jhcm.base.http.HttpProperties;
import io.github.ntduycs.jhcm.base.http.interceptor.model.CaseInsensitiveHeaders;
import io.github.ntduycs.jhcm.base.util.JsonUtils;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.slf4j.MDC;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.lang.NonNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class ServerResponseLoggingInterceptor extends ServerHttpResponseDecorator {
  private final StringBuilder responseBody = new StringBuilder();
  private int responseLength = 0;

  private static final int PRINTOUT_LENGTH = 500;

  public ServerResponseLoggingInterceptor(
      ServerHttpResponse delegate, HttpProperties.Logging.Response properties, Instant startTime) {
    super(delegate);

    delegate.beforeCommit(
        () -> {
          if (!shouldLogResponse(delegate)) {
            return Mono.empty();
          }

          if (properties.isEnabled()) {
            log.info(
                "==> Response: [{}] {} {} {} {}ms - {} bytes",
                requireNonNullElse(MDC.get(REQUEST_ID), ""),
                requireNonNullElse(MDC.get(METHOD), "-"),
                requireNonNullElse(MDC.get(PATH), "-"),
                delegate.getStatusCode(),
                Duration.between(startTime, Instant.now()).toMillis(),
                responseLength);
            logBody(delegate);
          }

          if (properties.isIncludeHeaders()) {
            log.debug(
                "==> Headers: {}",
                JsonUtils.stringify(CaseInsensitiveHeaders.from(delegate.getHeaders())));
          }

          return Mono.empty();
        });
  }

  private boolean shouldLogResponse(ServerHttpResponse response) {
    if (response.getHeaders().getContentType() == null) {
      return true;
    }

    return Stream.of("image", "octet-stream", "javascript", "css", "html")
        .noneMatch(s -> response.getHeaders().getContentType().toString().contains(s));
  }

  private boolean shouldLogBody(ServerHttpResponse response) {
    return Optional.ofNullable(response.getHeaders().getContentType())
        .map(MediaType::toString)
        .map(String::toLowerCase)
        .map(s -> s.contains("json"))
        .orElse(false);
  }

  private void logBody(ServerHttpResponse response) {
    if (shouldLogBody(response)) {
      log.debug("==> Body: {}", responseBody.isEmpty() ? "{}" : responseBody.toString());
    } else {
      log.debug("==> Body: (omitted)");
    }
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
    var body = buffer.toString(StandardCharsets.UTF_8);

    responseLength += body.length();

    if (body.length() > PRINTOUT_LENGTH) {
      body = body.substring(0, PRINTOUT_LENGTH) + "...";
    }

    responseBody.append(body);
    return buffer;
  }
}
