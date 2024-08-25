package io.github.ntduycs.jhcm.account.domain.entity;

import static io.github.ntduycs.jhcm.base.domain.enums.Status.ACTIVE;

import io.github.ntduycs.jhcm.base.domain.entity.Reviewable;
import io.github.ntduycs.jhcm.base.domain.enums.Status;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Level extends Reviewable {
  private Integer id;
  private String code;
  private Status status = ACTIVE;
}
