package io.github.ntduycs.jhcm.base.scheduler.listener;

import io.github.ntduycs.jhcm.base.scheduler.JobRecord;
import io.github.ntduycs.jhcm.base.scheduler.JobRecorder;
import io.github.ntduycs.jhcm.base.scheduler.Retryable;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.listeners.TriggerListenerSupport;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuditTriggerListener extends TriggerListenerSupport {
  private final JobRecorder jobRecorder;

  @Override
  public String getName() {
    return "AuditTriggerListener";
  }

  @Override
  public void triggerComplete(
      Trigger trigger,
      JobExecutionContext context,
      Trigger.CompletedExecutionInstruction instructionCode) {
    jobRecorder
        .get(context.getFireInstanceId())
        .ifPresent(
            record -> {
              record
                  .setJobResult(context.getResult())
                  .setCompleteInstruction(instructionCode.name())
                  .setModifiedAt(Instant.now());
              jobRecorder.recordComplete(record);
            });
  }

  @Override
  public void triggerFired(Trigger trigger, JobExecutionContext context) {
    var job = context.getJobDetail();

    var jobRecord =
        new JobRecord()
            .setJobName(job.getKey().getName())
            .setJobGroup(job.getKey().getGroup())
            .setJobDataMap(job.getJobDataMap())
            .setJobRunTime(context.getJobRunTime())
            .setTriggerName(trigger.getKey().getName())
            .setTriggerPriority(trigger.getPriority())
            .setTriggerMayFireAgain(trigger.mayFireAgain())
            .setTriggerState(getTriggerState(trigger, context))
            .setFireInstanceId(context.getFireInstanceId())
            .setFireTime(context.getFireTime().toInstant())
            .setScheduledFireTime(context.getScheduledFireTime().toInstant())
            .setCreatedAt(Instant.now());

    if (job.getJobClass().isAnnotationPresent(Retryable.class)) {
      var retryable = job.getJobClass().getAnnotation(Retryable.class);
      jobRecord.setJobRetryMax(retryable.maxRetries());
    }

    jobRecorder.recordNew(jobRecord);
  }

  private String getTriggerState(Trigger trigger, JobExecutionContext context) {
    try {
      return context.getScheduler().getTriggerState(trigger.getKey()).name();
    } catch (SchedulerException e) {
      log.error("Failed to get trigger state", e);
      return "UNKNOWN";
    }
  }
}
