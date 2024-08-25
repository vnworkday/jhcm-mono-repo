package io.github.ntduycs.jhcm.{{service}}.controller.{{module}};

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class {{Usecase}}ControllerTest {
  @Autowired
  private WebTestClient mockMvc;

  private WebTestClient.ResponseSpec call{{Usecase}}Api() {
    return null;
  }
}
