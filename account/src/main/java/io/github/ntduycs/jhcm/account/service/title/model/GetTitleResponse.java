package io.github.ntduycs.jhcm.account.service.title.model;

import io.github.ntduycs.jhcm.base.domain.enums.Status;
import lombok.Data;

@Data
public class GetTitleResponse {
  private String code;
  private String name;
  private String description;
  private Integer version;
  private Status status;
}
