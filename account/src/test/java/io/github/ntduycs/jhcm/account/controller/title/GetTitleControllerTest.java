package io.github.ntduycs.jhcm.account.controller.title;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS;

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
  scripts = {"/db/sql/cleanup.sql", "/db/sql/title/titles.sql"},
  executionPhase = BEFORE_TEST_CLASS)
public class GetTitleControllerTest {
  @Autowired
  private WebTestClient mockMvc;

  private WebTestClient.ResponseSpec callGetTitleApi(String code) {
    return mockMvc.get().uri("/titles/{code}", code).exchange();
  }

  @Test
  @DisplayName("should return 200 when title exists")
  void shouldReturn200WhenTitleExists() {
    callGetTitleApi("title1")
      .expectStatus()
      .isOk()
      .expectBody()
      .jsonPath("$.code")
      .isEqualTo("title1");
  }

  @Test
  @DisplayName("should return 404 when title does not exist")
  void shouldReturn404WhenTitleDoesNotExist() {
    callGetTitleApi("title999")
      .expectStatus()
      .isNotFound()
      .expectBody()
      .jsonPath("$.errors")
      .isNotEmpty()
      .jsonPath("$.errors[0].code")
      .isEqualTo(HttpError.NOT_FOUND.getCode());
  }
}
