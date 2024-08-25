package io.github.ntduycs.jhcm.base.scheduler;

import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.InitializingBean;

@RequiredArgsConstructor
@Slf4j
public class JobManager implements InitializingBean {
  private final Scheduler scheduler;

  private final Collection<JobDetail> jobs;
  private final Collection<Trigger> triggers;

  /**
   * List all job statuses.
   *
   * @see <a href="https://stackoverflow.com/a/31479434/285571">Stackoverflow</a>
   */
  public List<Map<String, String>> listStatuses(String groupName) throws SchedulerException {
    var jobStatuses = new LinkedList<Map<String, String>>();

    for (var jobKey : scheduler.getJobKeys(GroupMatcher.groupEquals(groupName))) {
      var jobDetail = scheduler.getJobDetail(jobKey);
      var jobTriggers = scheduler.getTriggersOfJob(jobKey);

      for (var trigger : jobTriggers) {
        var state = scheduler.getTriggerState(trigger.getKey());
        var jobStatus =
            Map.of(
                "jobName", jobDetail.getKey().getName(),
                "triggerName", trigger.getKey().getName(),
                "state", state.toString());

        jobStatuses.add(jobStatus);
      }
    }

    jobStatuses.sort(Comparator.comparing(j -> j.get("jobName")));

    return jobStatuses;
  }

  public void add(JobDetail job) throws SchedulerException {
    if (!scheduler.checkExists(job.getKey())) {
      scheduler.addJob(job, false);
      log.info("Job {} added", job.getKey().getName());
    }
  }

  public void addAll(Collection<JobDetail> jobs) throws SchedulerException {
    for (var job : jobs) {
      add(job);
    }
  }

  public void delete(JobKey key) throws SchedulerException {
    scheduler.deleteJob(key);
    log.info("Job {} and its associated trigger(s) was deleted", key.getName());
  }

  public void schedule(Trigger trigger) throws SchedulerException {
    if (!scheduler.checkExists(trigger.getKey())) {
      var firstFireTime = scheduler.scheduleJob(trigger);
      log.info(
          "Job {} scheduled to first run at: {}",
          trigger.getJobKey().getName(),
          firstFireTime.toInstant());
    } else {
      var existingTrigger = scheduler.getTrigger(trigger.getKey());
      if (isTriggerUpdated(existingTrigger, trigger)) {
        scheduler.rescheduleJob(trigger.getKey(), trigger);
        log.info(
            "Trigger {} was rescheduled with next run at: {}",
            trigger.getKey().getName(),
            trigger.getNextFireTime().toInstant());
      }
    }
  }

  public void scheduleAll(Collection<Trigger> triggers) throws SchedulerException {
    for (var trigger : triggers) {
      schedule(trigger);
    }
  }

  private boolean isTriggerUpdated(Trigger existingTrigger, Trigger newTrigger) {
    return !Objects.equals(existingTrigger.getCalendarName(), newTrigger.getCalendarName())
        || !Objects.equals(existingTrigger.getDescription(), newTrigger.getDescription())
        || !Objects.equals(existingTrigger.getJobDataMap(), newTrigger.getJobDataMap())
        || existingTrigger.getMisfireInstruction() != newTrigger.getMisfireInstruction()
        || existingTrigger.getPriority() != newTrigger.getPriority();
  }

  public void schedule(JobDetail job, Trigger trigger) throws SchedulerException {
    add(job);
    schedule(trigger);
  }

  public void unschedule(TriggerKey key) throws SchedulerException {
    if (scheduler.unscheduleJob(key)) {
      log.info("Trigger {} was unscheduled", key.getName());
    }
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    log.info("Initializing jobs and triggers");
    addAll(jobs);
    scheduleAll(triggers);
  }
}
