package io.github.ntduycs.jhcm.account.controller.user;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS;

import io.github.ntduycs.jhcm.account.domain.repository.UserRepository;
import io.github.ntduycs.jhcm.base.http.exception.model.HttpError;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Sql(
    scripts = {"/db/sql/cleanup.sql", "/db/sql/user/users.sql"},
    executionPhase = BEFORE_TEST_CLASS)
public class GetUserControllerTest {
  @Autowired private WebTestClient mockMvc;
  @Autowired private UserRepository userRepository;

  private WebTestClient.ResponseSpec callGetUserApi(String code) {
    return mockMvc.get().uri("/users/" + code).exchange();
  }

  @Test
  @DisplayName("should return 200 when user exists")
  void shouldReturn200WhenUserExists() {
    var user = userRepository.findById(1).orElseThrow(AssertionError::new);

    callGetUserApi("user1")
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.code")
        .isEqualTo(user.getCode());
  }

  @Test
  @DisplayName("should return 404 when user does not exist")
  void shouldReturn404WhenUserDoesNotExist() {
    callGetUserApi("user999")
        .expectStatus()
        .isNotFound()
        .expectBody()
        .jsonPath("$.errors")
        .isNotEmpty()
        .jsonPath("$.errors[0].code")
        .isEqualTo(HttpError.NOT_FOUND.getCode());
  }
}
