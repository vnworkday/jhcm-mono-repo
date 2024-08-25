package io.github.ntduycs.jhcm.base.http;

import io.github.ntduycs.jhcm.base.http.filter.ServerLoggingFilter;
import io.github.ntduycs.jhcm.base.http.handler.ClientLoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.util.concurrent.TimeUnit;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.netty.http.client.HttpClient;

@Configuration
public class HttpConfig {

  @Bean
  @ConfigurationProperties(prefix = "http")
  public HttpProperties httpProperties() {
    return new HttpProperties();
  }

  @Bean
  public ServerLoggingFilter serverLoggingFilter() {
    return new ServerLoggingFilter(httpProperties());
  }

  @Bean
  public ClientLoggingHandler clientLoggingHandler() {
    return new ClientLoggingHandler(httpProperties());
  }

  @Bean
  public HttpClient httpClient() {
    return HttpClient.create()
        .doOnConnected(
            conn -> {
              conn.addHandlerLast(clientLoggingHandler());
              conn.addHandlerLast(
                  new ReadTimeoutHandler(httpProperties().getReadTimeout(), TimeUnit.MILLISECONDS));
              conn.addHandlerLast(
                  new WriteTimeoutHandler(
                      httpProperties().getWriteTimeout(), TimeUnit.MILLISECONDS));
            });
  }
}
