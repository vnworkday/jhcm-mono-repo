package io.github.ntduycs.jhcm.base.domain;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DefaultDatasourceConfig {

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
  public SqlSessionFactory mainSqlSessionFactory() throws Exception {
    var factory = new SqlSessionFactoryBean();
    factory.setDataSource(mainDataSource());
    return factory.getObject();
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

  @Bean
  @ConditionalOnProperty(value = "spring.quartz.auto-startup", havingValue = "true")
  public SqlSessionFactory quartzSqlSessionFactory() throws Exception {
    var factory = new SqlSessionFactoryBean();
    factory.setDataSource(quartzDataSource());
    return factory.getObject();
  }
}
