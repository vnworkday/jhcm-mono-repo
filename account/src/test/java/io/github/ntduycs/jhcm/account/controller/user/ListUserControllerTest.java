package io.github.ntduycs.jhcm.account.controller.user;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS;

import io.github.ntduycs.jhcm.account.domain.entity.User;
import io.github.ntduycs.jhcm.account.domain.repository.UserRepository;
import io.github.ntduycs.jhcm.account.service.user.model.ListUserRequest;
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
    scripts = {"/db/sql/cleanup.sql", "/db/sql/user/users.sql"},
    executionPhase = BEFORE_TEST_CLASS)
public class ListUserControllerTest {
  @Autowired private WebTestClient mockMvc;
  @Autowired private UserRepository userRepository;

  private ListUserRequest request;
  private int totalUsers;
  private List<User> users;

  @BeforeEach
  void setUp() {
    request = new ListUserRequest();
    users = userRepository.findAll();
    totalUsers = users.size();
  }

  private WebTestClient.ResponseSpec callListUsersApi(ListUserRequest request) {
    return mockMvc
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("/users")
                    .queryParam("offset", request.getOffset())
                    .queryParam("size", request.getSize())
                    .queryParam("sort", request.getSort())
                    .queryParam("order", request.getOrder())
                    .queryParam("username", request.getUsername())
                    .queryParam("name", request.getName())
                    .build())
        .exchange();
  }

  @Test
  @DisplayName("should return 200 with all users")
  void shouldReturn200WithAllUsers() {
    callListUsersApi(request)
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.total")
        .isEqualTo(totalUsers)
        .jsonPath("$.totalPages")
        .isEqualTo(1)
        .jsonPath("$.items.length()")
        .isEqualTo(totalUsers)
        .jsonPath("$.offset")
        .isEqualTo(request.getOffset())
        .jsonPath("$.size")
        .isEqualTo(request.getSize());
  }

  @Test
  @DisplayName("should return 200 with filtered users")
  void shouldReturn200WithFilteredUsers() {
    request.setUsername(users.getFirst().getUsername());
    request.setName(users.getFirst().getFirstName());

    callListUsersApi(request)
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
        .isEqualTo(users.getFirst().getCode())
        .jsonPath("$.offset")
        .isEqualTo(request.getOffset())
        .jsonPath("$.size")
        .isEqualTo(request.getSize());
  }

  @Test
  @DisplayName("should return 200 with paginated users")
  void shouldReturn200WithPaginatedUsers() {
    request.setSize(1);

    callListUsersApi(request)
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.total")
        .isEqualTo(totalUsers)
        .jsonPath("$.totalPages")
        .isEqualTo(totalUsers)
        .jsonPath("$.items.length()")
        .isEqualTo(1)
        .jsonPath("$.offset")
        .isEqualTo(request.getOffset())
        .jsonPath("$.size")
        .isEqualTo(request.getSize());
  }

  @Test
  @DisplayName("should return 200 with sorted users")
  void shouldReturn200WithSortedUsers() {
    request.setSort("username");
    request.setOrder("desc");

    users.sort((a, b) -> b.getUsername().compareTo(a.getUsername()));

    callListUsersApi(request)
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.total")
        .isEqualTo(totalUsers)
        .jsonPath("$.totalPages")
        .isEqualTo(1)
        .jsonPath("$.items.length()")
        .isEqualTo(totalUsers)
        .jsonPath("$.offset")
        .isEqualTo(request.getOffset())
        .jsonPath("$.size")
        .isEqualTo(request.getSize())
        .jsonPath("$.items[0].code")
        .isEqualTo(users.getLast().getCode())
        .jsonPath("$.items[1].code")
        .isEqualTo(users.get(1).getCode())
        .jsonPath("$.items[2].code")
        .isEqualTo(users.get(0).getCode());
  }

  @Test
  @DisplayName("should return 200 with empty users when username is unknown")
  void shouldReturn200WithEmptyUsers() {
    request.setUsername("unknown");

    callListUsersApi(request)
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.total")
        .isEqualTo(0)
        .jsonPath("$.totalPages")
        .isEqualTo(0)
        .jsonPath("$.items.length()")
        .isEqualTo(0)
        .jsonPath("$.offset")
        .isEqualTo(request.getOffset())
        .jsonPath("$.size")
        .isEqualTo(request.getSize());
  }

  @Test
  @DisplayName("should return 400 when offset is invalid")
  void shouldReturn400WhenOffsetIsInvalid() {
    request.setOffset(-1);

    callListUsersApi(request)
        .expectStatus()
        .isBadRequest()
        .expectBody()
        .jsonPath("$.errors.length()")
        .isEqualTo(1)
        .jsonPath("$.errors[0].code")
        .isEqualTo(HttpError.BAD_REQUEST.getCode());
  }
}
