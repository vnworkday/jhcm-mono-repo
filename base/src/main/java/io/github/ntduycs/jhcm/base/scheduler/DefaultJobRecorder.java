package io.github.ntduycs.jhcm.base.scheduler;

import io.github.ntduycs.jhcm.base.util.JsonUtils;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnMissingBean(JobRecorder.class)
public class DefaultJobRecorder implements JobRecorder, InitializingBean {
  @Override
  public void recordNew(JobRecord jobRecord) {
    log.info("Recording new job: {}", JsonUtils.stringify(jobRecord));
  }

  @Override
  public void recordComplete(JobRecord jobRecord) {
    log.info("Recording completed job: {}", JsonUtils.stringify(jobRecord));
  }

  @Override
  public Optional<JobRecord> get(String fireInstanceId) {
    log.warn("Cannot get job record. Consider to implement JobRecorder for persistent storage");

    return Optional.empty();
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    log.warn("Using DefaultJobRecorder. Consider to implement JobRecorder for persistent storage");
  }
}
