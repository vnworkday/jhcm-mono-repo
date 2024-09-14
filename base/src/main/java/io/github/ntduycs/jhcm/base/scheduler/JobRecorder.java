package io.github.ntduycs.jhcm.base.scheduler;

import java.util.Optional;

public interface JobRecorder {
  void recordNew(JobRecord jobRecord);

  void recordComplete(JobRecord jobRecord);

  Optional<JobRecord> get(String fireInstanceId);
}
