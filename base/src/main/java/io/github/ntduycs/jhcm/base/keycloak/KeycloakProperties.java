package io.github.ntduycs.jhcm.base.keycloak;

import lombok.Data;

@Data
public class KeycloakProperties {
  private boolean enabled;
  private String url;
  private String realm;
  private String clientId;
  private String clientSecret;
}
