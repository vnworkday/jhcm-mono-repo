package io.github.ntduycs.jhcm.account.service.title.model;

import static io.github.ntduycs.jhcm.base.http.HttpConstant.*;
import static jakarta.validation.constraints.Pattern.Flag.CASE_INSENSITIVE;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ListTitleRequest {
  @Min(0)
  private Integer offset = HTTP_REQUEST_DEFAULT_OFFSET;

  @Min(1)
  private Integer size = HTTP_REQUEST_DEFAULT_SIZE;

  private String sort = HTTP_REQUEST_DEFAULT_SORT;

  @Pattern(regexp = "asc|desc", flags = CASE_INSENSITIVE)
  private String order = HTTP_REQUEST_DEFAULT_ORDER;

  private String name;

  @Pattern(regexp = "[ai]", flags = CASE_INSENSITIVE)
  private String status;
}
