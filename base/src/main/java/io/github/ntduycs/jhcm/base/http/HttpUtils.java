package io.github.ntduycs.jhcm.base.http;

import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class HttpUtils {

  public static int calculateTotalPages(int total, int size) {
    return (int) Math.ceil((double) total / size);
  }
}
