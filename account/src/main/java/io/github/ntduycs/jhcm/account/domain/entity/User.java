package io.github.ntduycs.jhcm.account.domain.entity;

import io.github.ntduycs.jhcm.base.domain.entity.Reviewable;
import io.github.ntduycs.jhcm.base.domain.enums.Status;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class User extends Reviewable {
  private Integer id;
  private String code;
  private String cifNumber;
  private String username;
  private String email;
  private String firstName;
  private String lastName;
  private Status status;
  private Integer titleId;
}
