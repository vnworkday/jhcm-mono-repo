package io.github.ntduycs.jhcm.account.service.title.model;

import static io.github.ntduycs.jhcm.base.http.HttpConstant.*;

import io.github.ntduycs.jhcm.base.domain.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ListTitleRequest {
  @Min(0)
  @Schema(description = "Zero-based offset", defaultValue = "0")
  private Integer offset = HTTP_REQUEST_DEFAULT_OFFSET;

  @Min(1)
  @Schema(description = "Number of elements per page", defaultValue = "10")
  private Integer size = HTTP_REQUEST_DEFAULT_SIZE;

  @Pattern(regexp = "name|status|updatedAt")
  @Schema(
      description = "Sort field",
      defaultValue = "updatedAt",
      allowableValues = {"name", "status", "updatedAt"})
  private String sort = HTTP_REQUEST_DEFAULT_SORT;

  @Pattern(regexp = "asc|desc")
  @Schema(
      description = "Sort order",
      defaultValue = "desc",
      allowableValues = {"asc", "desc"})
  private String order = HTTP_REQUEST_DEFAULT_ORDER;

  @Schema(description = "Search by name containing")
  private String name;

  @Pattern(regexp = "[AI]")
  @Schema(
      description = "Search by status (A: Active, I: Inactive)",
      oneOf = {Status.class})
  private String status;
}
