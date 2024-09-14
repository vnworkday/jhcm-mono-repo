package io.github.ntduycs.jhcm.base.domain.entity;

import java.io.Serializable;
import java.time.Instant;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public abstract class Auditable implements Serializable {
  private Instant createdAt;
  private Instant updatedAt;
}
