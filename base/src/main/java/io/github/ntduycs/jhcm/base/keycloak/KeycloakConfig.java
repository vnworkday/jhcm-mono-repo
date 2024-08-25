package io.github.ntduycs.jhcm.base.keycloak;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
@ConditionalOnProperty(value = "keycloak.enabled", havingValue = "true")
@Slf4j
public class KeycloakConfig implements InitializingBean {

  @Bean
  @ConfigurationProperties(prefix = "keycloak")
  public KeycloakProperties keycloakProperties() {
    return new KeycloakProperties();
  }

  @Bean
  public WebClient keycloakWebClient(KeycloakProperties properties, HttpClient httpClient) {
    return WebClient.builder()
        .baseUrl(properties.getUrl())
        .clientConnector(new ReactorClientHttpConnector(httpClient))
        .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .build();
  }

  @Override
  public void afterPropertiesSet() {
    log.debug("========== Keycloak initialized ==========");
  }
}
