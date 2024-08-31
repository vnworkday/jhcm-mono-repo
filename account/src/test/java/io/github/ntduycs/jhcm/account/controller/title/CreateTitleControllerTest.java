package io.github.ntduycs.jhcm.account.controller.title;

import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.ntduycs.jhcm.account.domain.repository.TitleRepository;
import io.github.ntduycs.jhcm.account.service.title.model.CreateTitleRequest;
import io.github.ntduycs.jhcm.base.domain.enums.Status;
import io.github.ntduycs.jhcm.base.http.exception.model.HttpError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Sql(
    scripts = {"/db/sql/cleanup.sql", "/db/sql/title/titles.sql"},
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
public class CreateTitleControllerTest {
  @Autowired private WebTestClient mockMvc;
  @Autowired private TitleRepository titleRepository;

  private WebTestClient.ResponseSpec callCreateTitleApi(CreateTitleRequest request) {
    return mockMvc
        .post()
        .uri("/titles")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(request)
        .exchange();
  }

  private CreateTitleRequest request;

  @BeforeEach
  void setUp() {
    request = new CreateTitleRequest().setName("New Title").setDescription("Description 1");
  }

  @Test
  @DisplayName("should return 201 when title is created")
  void shouldReturn201WhenTitleIsCreated() {
    callCreateTitleApi(request)
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.code")
        .isNotEmpty()
        .jsonPath("$.status")
        .isEqualTo(Status.ACTIVE.getCode())
        .jsonPath("$.createdAt")
        .isNotEmpty()
        .jsonPath("$.version")
        .isEqualTo(1)
        .jsonPath("$.errors")
        .doesNotExist();

    assertTrue(() -> titleRepository.findByName(request.getName()).isPresent());
  }

  @Test
  @DisplayName("should return 400 when title name is empty")
  void shouldReturn400WhenTitleNameIsEmpty() {
    request.setName("");

    callCreateTitleApi(request)
        .expectStatus()
        .isBadRequest()
        .expectBody()
        .jsonPath("$.errors")
        .isNotEmpty()
        .jsonPath("$.errors[0].code")
        .isEqualTo(HttpError.BAD_REQUEST.getCode());

    assertTrue(() -> titleRepository.findByName(request.getName()).isEmpty());
  }

  @Test
  @DisplayName("should return 409 when title name is already taken")
  void shouldReturn409WhenTitleNameIsAlreadyTaken() {
    request.setName("Title 2");

    assertTrue(() -> titleRepository.findByName(request.getName()).isPresent());

    callCreateTitleApi(request)
        .expectStatus()
        .isEqualTo(HttpStatus.CONFLICT)
        .expectBody()
        .jsonPath("$.errors")
        .isNotEmpty()
        .jsonPath("$.errors[0].code")
        .isEqualTo(HttpError.ALREADY_EXISTS.getCode());
  }
}
