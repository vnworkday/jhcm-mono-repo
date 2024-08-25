package io.github.ntduycs.jhcm.base.scheduler;

import java.time.Instant;
import java.util.Map;
import lombok.Data;

@Data
public class JobRecord {
  private String fireInstanceId;

  private String jobName;
  private String jobGroup;
  private Map<String, Object> jobDataMap;
  private Integer jobRetryMax;
  private Long jobRunTime; // In milliseconds, updated when job is completed
  private Object jobResult; // Updated when job is completed

  private String triggerName;
  private Integer triggerPriority;
  private Boolean triggerMayFireAgain;
  private String triggerState; // Changed on each trigger event

  private Instant fireTime;
  private Instant scheduledFireTime;
  private String completeInstruction; // Updated when job is completed

  private Instant createdAt;
  private Instant modifiedAt;
}
