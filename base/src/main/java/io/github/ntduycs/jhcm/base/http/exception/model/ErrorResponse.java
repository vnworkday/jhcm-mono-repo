package io.github.ntduycs.jhcm.base.http.exception.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ErrorResponse {
  private List<Detail> errors = new ArrayList<>();

  public ErrorResponse(HttpError error, String detail) {
    this.errors =
        List.of(new Detail().setCode(error.getCode()).setTitle(error.getTitle()).setDetail(detail));
  }

  public void addError(HttpError error, String detail) {
    errors.add(new Detail().setCode(error.getCode()).setTitle(error.getTitle()).setDetail(detail));
  }

  @Data
  public static class Detail {
    private int code;
    private String title;
    private String detail;
    private boolean retryable;
  }
}
