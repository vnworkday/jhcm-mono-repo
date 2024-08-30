package io.github.ntduycs.jhcm.account.service.title.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateTitleRequest {
  @NotBlank private String name;
  private String description = "";
}
