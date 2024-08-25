package io.github.ntduycs.jhcm.base.domain.entity;

import io.github.ntduycs.jhcm.base.domain.enums.ChangeStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class Reviewable extends Versional {
  private ChangeStatus changeStatus;
}
