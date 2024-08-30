package io.github.ntduycs.jhcm.account.domain.enums;

import lombok.Getter;

@Getter
public enum ResourceType {
  TITLE;
  private final String code;

  ResourceType() {
    this.code = name().toLowerCase();
  }

  public static ResourceType fromCode(String code) {
    for (ResourceType resource : values()) {
      if (resource.getCode().equals(code)) {
        return resource;
      }
    }
    throw new IllegalArgumentException("Unknown resource type: " + code);
  }
}
