package io.github.ntduycs.jhcm.account.domain.entity;

import static io.github.ntduycs.jhcm.base.domain.enums.Status.ACTIVE;

import io.github.ntduycs.jhcm.base.domain.entity.Versional;
import io.github.ntduycs.jhcm.base.domain.enums.Status;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Title extends Versional {
  private Integer id;
  private String code;
  private String name;
  private String description = "";
  private Status status = ACTIVE;
}
