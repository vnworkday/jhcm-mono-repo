package io.github.ntduycs.jhcm.{{service}}.domain.entity;

import io.github.ntduycs.jhcm.base.domain.entity.Reviewable;
import io.github.ntduycs.jhcm.base.domain.enums.Status;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static io.github.ntduycs.jhcm.base.domain.enums.Status.ACTIVE;

@Data
@EqualsAndHashCode(callSuper = true)
public class {{Entity}} extends Reviewable {
  private Integer id;
  private String code;
  private Status status = ACTIVE;
}
