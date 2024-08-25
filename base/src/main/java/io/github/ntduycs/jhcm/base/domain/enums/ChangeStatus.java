package io.github.ntduycs.jhcm.base.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ChangeStatus {
  WAITING("W"),
  APPROVED("A"),
  REJECTED("R");

  @JsonValue private final String code;

  public static ChangeStatus fromCode(String code) {
    for (ChangeStatus status : values()) {
      if (status.code.equals(code)) {
        return status;
      }
    }
    return null;
  }
}
