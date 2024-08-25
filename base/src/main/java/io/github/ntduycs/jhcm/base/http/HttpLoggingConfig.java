package io.github.ntduycs.jhcm.base.http;

import io.github.ntduycs.jhcm.base.http.filter.ServerLoggingFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpLoggingConfig {

  @Bean
  @ConfigurationProperties(prefix = "logging.http")
  public HttpLoggingProperties httpLoggingProperties() {
    return new HttpLoggingProperties();
  }

  @Bean
  public ServerLoggingFilter serverLoggingFilter() {
    return new ServerLoggingFilter(httpLoggingProperties());
  }
}
