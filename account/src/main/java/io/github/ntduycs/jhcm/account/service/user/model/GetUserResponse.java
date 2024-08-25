package io.github.ntduycs.jhcm.account.service.user.model;

import io.github.ntduycs.jhcm.base.domain.enums.ChangeStatus;
import io.github.ntduycs.jhcm.base.domain.enums.Status;
import lombok.Data;

@Data
public class GetUserResponse {
  private String code;
  private String cifNumber;
  private String username;
  private String email;
  private String firstName;
  private String lastName;
  private Integer version;
  private ChangeStatus changeStatus;
  private Status status;
}
