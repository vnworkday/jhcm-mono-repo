package io.github.ntduycs.jhcm.account.service.title.model;

import io.github.ntduycs.jhcm.base.domain.enums.Status;
import java.time.Instant;
import lombok.Data;

@Data
public class GetTitleResponse {
  private String code;
  private String name;
  private String description;
  private Status status;
  private Instant createdAt;
  private Instant updatedAt;
  private User creator;
  private User updater;
  private Integer version;

  @Data
  public static class User {
    private String code;
    private String cifNumber;
    private String firstName;
    private String lastName;
  }
}
