package io.github.ntduycs.jhcm.base.domain;

import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@Slf4j
public class DatasourceConfig implements InitializingBean {

  @Bean
  @ConfigurationProperties(prefix = "spring.datasource.main")
  public DataSourceProperties mainDataSourceProperties() {
    return new DataSourceProperties();
  }

  @Bean
  @Primary
  public DataSource mainDataSource() {
    return mainDataSourceProperties().initializeDataSourceBuilder().build();
  }

  @Bean
  @ConfigurationProperties(prefix = "spring.datasource.quartz")
  @ConditionalOnProperty(value = "spring.quartz.auto-startup", havingValue = "true")
  public DataSourceProperties quartzDataSourceProperties() {
    return new DataSourceProperties();
  }

  @Bean
  @QuartzDataSource
  @ConditionalOnProperty(value = "spring.quartz.auto-startup", havingValue = "true")
  public DataSource quartzDataSource() {
    return quartzDataSourceProperties().initializeDataSourceBuilder().build();
  }

  @Override
  public void afterPropertiesSet() {
    log.debug("========== Datasource initialized ==========");
  }
}
