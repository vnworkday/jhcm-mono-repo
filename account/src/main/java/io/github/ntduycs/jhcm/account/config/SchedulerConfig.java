package io.github.ntduycs.jhcm.account.config;

import io.github.ntduycs.jhcm.base.scheduler.DefaultJobRecorder;
import io.github.ntduycs.jhcm.base.scheduler.DefaultSchedulerConfig;
import io.github.ntduycs.jhcm.base.scheduler.JobManager;
import io.github.ntduycs.jhcm.base.scheduler.JobRecorder;
import java.io.IOException;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.spi.JobFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
@ConditionalOnProperty(value = "spring.quartz.auto-startup", havingValue = "true")
@ComponentScan("io.github.ntduycs.jhcm.base.scheduler.listener")
@Slf4j
public class SchedulerConfig extends DefaultSchedulerConfig {

  @Bean
  public JobFactory jobFactory(ApplicationContext context) {
    return defaultAutoWiringJobFactory(context);
  }

  @Bean
  public SchedulerFactoryBean schedulerFactoryBean(ApplicationContext context) throws IOException {
    var jobFactory = context.getBean(JobFactory.class);
    var datasource = context.getBean("quartzDataSource", DataSource.class);
    var jobListeners = context.getBeansOfType(JobListener.class).values();
    var triggerListeners = context.getBeansOfType(TriggerListener.class).values();

    log.info("Registering {} job listeners", jobListeners.size());
    log.info("Registering {} trigger listeners", triggerListeners.size());

    return defaultSchedulerFactoryBean(
        context, datasource, jobFactory, jobListeners, triggerListeners);
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

  @Bean
  @ConditionalOnMissingBean(JobRecorder.class)
  public JobRecorder defaultJobRecorder() {
    return new DefaultJobRecorder();
  }
}
