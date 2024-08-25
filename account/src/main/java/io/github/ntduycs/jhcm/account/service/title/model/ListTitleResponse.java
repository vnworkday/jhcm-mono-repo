package io.github.ntduycs.jhcm.account.service.title.model;

import java.util.List;
import lombok.Data;

@Data
public class ListTitleResponse {
  private Integer offset;
  private Integer size;
  private Integer total;
  private Integer totalPages;
  private List<Item> items;

  @Data
  public static class Item {
    private String code;
    private String name;
    private String description;
    private String status;
  }
}
