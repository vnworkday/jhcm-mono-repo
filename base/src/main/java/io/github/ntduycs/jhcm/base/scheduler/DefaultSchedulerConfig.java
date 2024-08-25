package io.github.ntduycs.jhcm.base.scheduler;

import java.io.IOException;
import java.util.Collection;
import java.util.Properties;
import javax.sql.DataSource;
import org.quartz.JobListener;
import org.quartz.TriggerListener;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.ClassPathResource;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

public class DefaultSchedulerConfig {
  protected static final String QUARTZ_PROPERTIES_FILE = "/quartz.properties";

  protected JobFactory defaultAutoWiringJobFactory(ApplicationContext context) {
    var factory = new AutowiringSpringBeanJobFactory();
    factory.setApplicationContext(context);
    return factory;
  }

  protected Properties quartzProperties() throws IOException {
    var propertiesFactoryBean = new PropertiesFactoryBean();
    propertiesFactoryBean.setLocation(new ClassPathResource(QUARTZ_PROPERTIES_FILE));
    propertiesFactoryBean.afterPropertiesSet();
    return propertiesFactoryBean.getObject();
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
}
