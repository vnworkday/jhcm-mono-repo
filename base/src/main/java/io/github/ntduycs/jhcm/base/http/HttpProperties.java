package io.github.ntduycs.jhcm.base.http;

import java.util.List;
import lombok.Data;

@Data
public class HttpProperties {
  private Logging logging;
  private Integer readTimeout;
  private Integer writeTimeout;

  @Data
  public static class Logging {
    private Request request;
    private Response response;

    @Data
    public static class Request {
      private boolean enabled;
      private boolean includeHeaders;
      private List<String> ignoredUris;
    }

    @Data
    public static class Response {
      private boolean enabled;
      private boolean includeHeaders;
      private List<String> ignoredUris;
    }
  }
}
