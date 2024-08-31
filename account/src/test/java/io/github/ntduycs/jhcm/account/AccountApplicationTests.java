package io.github.ntduycs.jhcm.account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AccountApplicationTests {
  @LocalServerPort private int port;

  @BeforeEach
  void setUp() {
    System.setProperty("server.port", String.valueOf(port));
  }

  @Test
  @DisplayName("should load context")
  void contextLoads() {}
}
