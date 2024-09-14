package io.github.ntduycs.jhcm.base.domain.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public abstract class Versional extends Auditable {
  private Integer createdBy;
  private Integer updatedBy;
  private Integer version;
}
