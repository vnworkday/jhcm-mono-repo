package io.github.ntduycs.jhcm.account.service.user.model;

import io.github.ntduycs.jhcm.base.domain.enums.ChangeStatus;
import io.github.ntduycs.jhcm.base.domain.enums.Status;
import java.util.List;
import lombok.Data;

@Data
public class ListUserResponse {
  private Integer offset;
  private Integer size;
  private Integer total;
  private Integer totalPages;
  private List<Item> items;

  @Data
  public static class Item {
    private String code;
    private String cifNumber;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private Status status;
    private ChangeStatus changeStatus;
  }
}
