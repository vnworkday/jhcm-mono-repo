package io.github.ntduycs.jhcm.base.scheduler;

import java.io.IOException;
import java.util.Collection;
import java.util.Properties;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

@Configuration
@ConditionalOnProperty(value = "spring.quartz.auto-startup", havingValue = "true")
@Slf4j
public class DefaultSchedulerConfig implements InitializingBean {
  protected static final String QUARTZ_PROPERTIES_FILE = "/quartz.properties";

  @Bean
  public JobFactory jobFactory(ApplicationContext context) {
    return defaultAutoWiringJobFactory(context);
  }

  protected JobFactory defaultAutoWiringJobFactory(ApplicationContext context) {
    var factory = new AutowiringSpringBeanJobFactory();
    factory.setApplicationContext(context);
    return factory;
  }

  @Bean
  protected Properties quartzProperties() throws IOException {
    var propertiesFactoryBean = new PropertiesFactoryBean();
    propertiesFactoryBean.setLocation(new ClassPathResource(QUARTZ_PROPERTIES_FILE));
    propertiesFactoryBean.afterPropertiesSet();
    return propertiesFactoryBean.getObject();
  }

  @Bean
  public SchedulerFactoryBean schedulerFactoryBean(ApplicationContext context) {
    try {
      var jobFactory = context.getBean(JobFactory.class);
      var dataSource = context.getBean("quartzDataSource", DataSource.class);
      var jobListeners = context.getBeansOfType(JobListener.class).values();
      var triggerListeners = context.getBeansOfType(TriggerListener.class).values();

      log.info("Registering {} job listeners", jobListeners.size());
      log.info("Registering {} trigger listeners", triggerListeners.size());

      return defaultSchedulerFactoryBean(
          context, dataSource, jobFactory, jobListeners, triggerListeners);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Bean
  public JobManager jobManager(ApplicationContext context) {
    var scheduler = context.getBean(Scheduler.class);
    var jobs = context.getBeansOfType(JobDetail.class).values();
    var triggers = context.getBeansOfType(Trigger.class).values();

    log.info("Registering {} jobs", jobs.size());
    log.info("Registering {} triggers", triggers.size());

    return new JobManager(scheduler, jobs, triggers);
  }

  protected SchedulerFactoryBean defaultSchedulerFactoryBean(
      ApplicationContext context,
      DataSource dataSource,
      JobFactory jobFactory,
      Collection<JobListener> jobListeners,
      Collection<TriggerListener> triggerListeners)
      throws IOException {
    var schedulerFactory = new SchedulerFactoryBean();
    schedulerFactory.setDataSource(dataSource);
    schedulerFactory.setJobFactory(jobFactory);
    schedulerFactory.setQuartzProperties(quartzProperties());
    schedulerFactory.setWaitForJobsToCompleteOnShutdown(true);
    schedulerFactory.setApplicationContext(context);
    schedulerFactory.setGlobalJobListeners(jobListeners.toArray(new JobListener[0]));
    schedulerFactory.setGlobalTriggerListeners(triggerListeners.toArray(new TriggerListener[0]));
    return schedulerFactory;
  }

  static class AutowiringSpringBeanJobFactory extends SpringBeanJobFactory
      implements ApplicationContextAware {
    private transient AutowireCapableBeanFactory beanFactory;

    @Override
    public void setApplicationContext(ApplicationContext context) {
      beanFactory = context.getAutowireCapableBeanFactory();
    }

    @Override
    @NonNull protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
      var job = super.createJobInstance(bundle);
      beanFactory.autowireBean(job);
      return job;
    }
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    log.debug("========== Scheduler initialized ==========");
  }
}
