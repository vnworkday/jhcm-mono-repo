package io.github.ntduycs.jhcm.base.scheduler.listener;

import io.github.ntduycs.jhcm.base.scheduler.Retryable;
import java.time.Instant;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.listeners.JobListenerSupport;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RetryJobListener extends JobListenerSupport {
  @Override
  public String getName() {
    return "RetryListener";
  }

  @Override
  public void jobWasExecuted(JobExecutionContext context, JobExecutionException e) {
    if (e == null) {
      return;
    }

    var job = context.getJobDetail();
    var trigger = context.getTrigger();

    if (job.getJobClass().isAnnotationPresent(Retryable.class)) {
      var retryable = job.getJobClass().getAnnotation(Retryable.class);
      var maxRetries = retryable.maxRetries();
      var currentRetries = (int) job.getJobDataMap().computeIfAbsent("retry", k -> 0);

      if (currentRetries < maxRetries) {
        retry(job, trigger, currentRetries, context);
      } else {
        log.error("Job {} failed after {} retries", job.getKey(), maxRetries);
      }
    }
  }

  private void retry(
      JobDetail job, Trigger trigger, int currentRetries, JobExecutionContext context) {
    var retryTrigger =
        TriggerBuilder.newTrigger()
            .forJob(job)
            .withPriority(trigger.getPriority())
            .withIdentity(
                generateRetryTriggerKey(trigger, currentRetries), trigger.getKey().getGroup())
            .withDescription(trigger.getDescription())
            .usingJobData(trigger.getJobDataMap())
            .usingJobData("retry", currentRetries + 1)
            .withSchedule(
                SimpleScheduleBuilder.simpleSchedule()
                    .withRepeatCount(0) // One-shot trigger
                    .withMisfireHandlingInstructionFireNow())
            .startAt(calculateRetryStartTime(trigger, currentRetries))
            .modifiedByCalendar(trigger.getCalendarName())
            .build();

    try {
      context.getScheduler().scheduleJob(job, retryTrigger);
    } catch (SchedulerException e) {
      throw new RuntimeException(e);
    }
  }

  private String generateRetryTriggerKey(Trigger trigger, int currentRetries) {
    return trigger.getKey().getName().contains("-retry-")
        ? trigger
            .getKey()
            .getName()
            .replace("-retry-" + (currentRetries - 1), "-retry-" + currentRetries)
        : trigger.getKey() + "-retry-" + currentRetries;
  }

  /**
   * Calculate the scheduled start time to retry the job. The time is calculated using backoff
   * strategy with a base delay of 3 second.
   */
  private Date calculateRetryStartTime(Trigger trigger, int currentRetries) {
    return Date.from(Instant.now().plusSeconds(3L * (currentRetries + 1)));
  }
}
