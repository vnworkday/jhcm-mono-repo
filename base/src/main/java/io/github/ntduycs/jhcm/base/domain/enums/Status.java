package io.github.ntduycs.jhcm.base.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Status {
  ACTIVE("A"),
  INACTIVE("I");

  @JsonValue private final String code;

  public static Status fromCode(String code) {
    for (Status status : values()) {
      if (status.code.equals(code)) {
        return status;
      }
    }
    return null;
  }
}
