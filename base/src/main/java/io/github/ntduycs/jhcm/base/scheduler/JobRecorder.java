package io.github.ntduycs.jhcm.base.scheduler;

public interface JobRecorder {
  void recordNew(JobRecord jobRecord);

  void recordComplete(JobRecord jobRecord);

  JobRecord get(String fireInstanceId);
}
