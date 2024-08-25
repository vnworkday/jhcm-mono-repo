package io.github.ntduycs.jhcm.base.scheduler;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Retryable {
  int maxRetries() default 3;
}
