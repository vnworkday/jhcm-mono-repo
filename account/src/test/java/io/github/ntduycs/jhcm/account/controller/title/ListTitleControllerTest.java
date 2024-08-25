package io.github.ntduycs.jhcm.account.controller.title;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS;

import io.github.ntduycs.jhcm.account.domain.entity.Title;
import io.github.ntduycs.jhcm.account.domain.repository.TitleRepository;
import io.github.ntduycs.jhcm.account.service.title.model.ListTitleRequest;
import io.github.ntduycs.jhcm.base.http.exception.model.HttpError;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
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
public class ListTitleControllerTest {
  @Autowired private WebTestClient mockMvc;
  @Autowired private TitleRepository titleRepository;

  private ListTitleRequest request;
  private int totalTitles;
  private List<Title> titles;

  @BeforeEach
  void setUp() {
    request = new ListTitleRequest();
    titles = titleRepository.findAll();
    totalTitles = titles.size();
  }

  private WebTestClient.ResponseSpec callListTitleApi() {
    return mockMvc
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("/titles")
                    .queryParam("offset", request.getOffset())
                    .queryParam("size", request.getSize())
                    .queryParam("sort", request.getSort())
                    .queryParam("order", request.getOrder())
                    .queryParam("status", request.getStatus())
                    .queryParam("name", request.getName())
                    .build())
        .exchange();
  }

  @Test
  @DisplayName("should return 200 with all titles")
  void shouldReturn200WithAllTitles() {
    callListTitleApi()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.total")
        .isEqualTo(totalTitles)
        .jsonPath("$.totalPages")
        .isEqualTo(1)
        .jsonPath("$.items.length()")
        .isEqualTo(totalTitles)
        .jsonPath("$.offset")
        .isEqualTo(request.getOffset())
        .jsonPath("$.size")
        .isEqualTo(request.getSize());
  }

  @Test
  @DisplayName("should return 200 with all titles sorted by name")
  void shouldReturn200WithAllTitlesSortedByName() {
    request.setSort("name");
    request.setOrder("asc");

    callListTitleApi()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.total")
        .isEqualTo(totalTitles)
        .jsonPath("$.totalPages")
        .isEqualTo(1)
        .jsonPath("$.items.length()")
        .isEqualTo(totalTitles)
        .jsonPath("$.offset")
        .isEqualTo(request.getOffset())
        .jsonPath("$.size")
        .isEqualTo(request.getSize());
  }

  @Test
  @DisplayName("should return 200 with all titles filtered by status")
  void shouldReturn200WithAllTitlesFilteredByStatus() {
    request.setStatus(titles.getFirst().getStatus().getCode());

    callListTitleApi()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.total")
        .isEqualTo(2)
        .jsonPath("$.totalPages")
        .isEqualTo(1)
        .jsonPath("$.items.length()")
        .isEqualTo(2)
        .jsonPath("$.items[0].code")
        .isEqualTo(titles.getFirst().getCode())
        .jsonPath("$.offset")
        .isEqualTo(request.getOffset())
        .jsonPath("$.size")
        .isEqualTo(request.getSize());
  }

  @Test
  @DisplayName("should return 200 with all titles filtered by name")
  void shouldReturn200WithAllTitlesFilteredByName() {
    request.setName(titles.getFirst().getName());

    callListTitleApi()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.total")
        .isEqualTo(1)
        .jsonPath("$.totalPages")
        .isEqualTo(1)
        .jsonPath("$.items.length()")
        .isEqualTo(1)
        .jsonPath("$.items[0].code")
        .isEqualTo(titles.getFirst().getCode())
        .jsonPath("$.offset")
        .isEqualTo(request.getOffset())
        .jsonPath("$.size")
        .isEqualTo(request.getSize());
  }

  @Test
  @DisplayName("should return 200 with paginated titles")
  void shouldReturn200WithPaginatedTitles() {
    request.setSize(1);

    callListTitleApi()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.total")
        .isEqualTo(totalTitles)
        .jsonPath("$.totalPages")
        .isEqualTo(totalTitles)
        .jsonPath("$.items.length()")
        .isEqualTo(1)
        .jsonPath("$.offset")
        .isEqualTo(request.getOffset())
        .jsonPath("$.size")
        .isEqualTo(request.getSize());
  }

  @Test
  @DisplayName("should return 200 with paginated titles sorted by name")
  void shouldReturn200WithPaginatedTitlesSortedByName() {
    request.setSize(1);
    request.setSort("name");
    request.setOrder("asc");

    callListTitleApi()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.total")
        .isEqualTo(totalTitles)
        .jsonPath("$.totalPages")
        .isEqualTo(totalTitles)
        .jsonPath("$.items.length()")
        .isEqualTo(1)
        .jsonPath("$.offset")
        .isEqualTo(request.getOffset())
        .jsonPath("$.size")
        .isEqualTo(request.getSize());
  }

  @Test
  @DisplayName("should return 200 with paginated titles filtered by status")
  void shouldReturn200WithPaginatedTitlesFilteredByStatus() {
    request.setSize(1);
    request.setStatus(titles.getFirst().getStatus().getCode());

    callListTitleApi()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.total")
        .isEqualTo(2)
        .jsonPath("$.totalPages")
        .isEqualTo(2)
        .jsonPath("$.items.length()")
        .isEqualTo(1)
        .jsonPath("$.items[0].code")
        .isEqualTo(titles.getFirst().getCode())
        .jsonPath("$.offset")
        .isEqualTo(request.getOffset())
        .jsonPath("$.size")
        .isEqualTo(request.getSize());
  }

  @Test
  @DisplayName("should return 400 when offset is invalid")
  void shouldReturn400WhenOffsetIsInvalid() {
    request.setOffset(-1);

    callListTitleApi()
        .expectStatus()
        .isBadRequest()
        .expectBody()
        .jsonPath("$.errors.length()")
        .isEqualTo(1)
        .jsonPath("$.errors[0].code")
        .isEqualTo(HttpError.BAD_REQUEST.getCode());
  }
}
