package io.github.ntduycs.jhcm.account.service.title.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateTitleRequest {
  @NotBlank
  @Schema(description = "Title name (unique)", maxLength = 255, example = "Software Engineer")
  private String name;

  @Schema(description = "Title description", maxLength = 1024, example = "Software Engineer")
  private String description = "";
}
