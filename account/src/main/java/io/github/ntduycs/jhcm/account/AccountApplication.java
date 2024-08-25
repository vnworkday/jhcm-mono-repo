package io.github.ntduycs.jhcm.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;

@SpringBootApplication(
    exclude = {QuartzAutoConfiguration.class},
    scanBasePackages = {"io.github.ntduycs.jhcm.account", "io.github.ntduycs.jhcm.base"})
public class AccountApplication {

  public static void main(String[] args) {
    SpringApplication.run(AccountApplication.class, args);
  }
}
