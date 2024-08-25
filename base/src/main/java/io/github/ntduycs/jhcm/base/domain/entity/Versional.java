package io.github.ntduycs.jhcm.base.domain.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class Versional extends Auditable {
  private Integer createdBy;
  private Integer updatedBy;
  private Integer version;
}
