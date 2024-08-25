package io.github.ntduycs.jhcm.account.service.user.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import io.github.ntduycs.jhcm.account.domain.entity.User;
import io.github.ntduycs.jhcm.account.domain.repository.UserRepository;
import io.github.ntduycs.jhcm.account.service.user.UserMapper;
import io.github.ntduycs.jhcm.account.service.user.model.ListUserRequest;
import io.github.ntduycs.jhcm.account.service.user.model.ListUserResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class ListUserUseCaseTest {

  @Mock private UserRepository userRepository;

  @Mock private UserMapper userMapper;

  @InjectMocks private ListUserUseCase listUserUseCase;

  private ListUserRequest request;
  private List<User> users;
  private List<ListUserResponse.Item> responseUsers;

  @BeforeEach
  void setUp() {
    request = new ListUserRequest().setName("name").setUsername("username");

    users =
        List.of(
            new User()
                .setUsername("name 1")
                .setEmail("email 1")
                .setFirstName("first name 1")
                .setLastName("last name 1"),
            new User()
                .setUsername("name 2")
                .setEmail("email 2")
                .setFirstName("first name 2")
                .setLastName("last name 2"));

    responseUsers =
        List.of(
            new ListUserResponse.Item()
                .setUsername("name 1")
                .setEmail("email 1")
                .setFirstName("first name 1")
                .setLastName("last name 1"),
            new ListUserResponse.Item()
                .setUsername("name 2")
                .setEmail("email 2")
                .setFirstName("first name 2")
                .setLastName("last name 2"));
  }

  @Test
  void successCase_givenValidRequest_whenUsersFound_thenReturnsUsers() {
    when(userRepository.search(request)).thenReturn(users);
    when(userRepository.count(request)).thenReturn(2);
    when(userMapper.fromUserToListResponse(any(User.class)))
        .thenAnswer(
            invocation -> {
              var user = invocation.getArgument(0, User.class);
              return new ListUserResponse.Item()
                  .setUsername(user.getUsername())
                  .setEmail(user.getEmail())
                  .setFirstName(user.getFirstName())
                  .setLastName(user.getLastName());
            });

    var result = listUserUseCase.handle(request);

    StepVerifier.create(result)
        .assertNext(
            response -> {
              assertEquals(2, response.getTotal());
              assertEquals(1, response.getTotalPages());
              assertEquals(responseUsers, response.getItems());
            })
        .verifyComplete();

    verify(userRepository, times(1)).search(request);
    verify(userRepository, times(1)).count(request);
  }

  @Test
  void successCase_givenValidRequest_whenNoUsersFound_thenReturnsEmptyList() {
    when(userRepository.search(request)).thenReturn(List.of());
    when(userRepository.count(request)).thenReturn(0);

    var result = listUserUseCase.handle(request);

    StepVerifier.create(result)
        .assertNext(
            response -> {
              assertEquals(0, response.getTotal());
              assertEquals(0, response.getTotalPages());
              assertTrue(response.getItems().isEmpty());
            })
        .verifyComplete();

    verify(userRepository, times(1)).search(request);
    verify(userRepository, times(1)).count(request);
  }
}
