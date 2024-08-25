package io.github.ntduycs.jhcm.base.scheduler;

import io.github.ntduycs.jhcm.base.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnMissingBean(JobRecorder.class)
public class DefaultJobRecorder implements JobRecorder {
  @Override
  public void recordNew(JobRecord jobRecord) {
    log.info("Recording new job: {}", JsonUtils.stringify(jobRecord));
  }

  @Override
  public void recordComplete(JobRecord jobRecord) {
    log.info("Recording completed job: {}", JsonUtils.stringify(jobRecord));
  }

  @Override
  public JobRecord get(String fireInstanceId) {
    log.warn(
        "Cannot get job record. Consider to implement JobRecorder instead of using DefaultJobRecorder");

    return new JobRecord().setFireInstanceId(fireInstanceId);
  }
}
