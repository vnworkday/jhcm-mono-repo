package io.github.ntduycs.jhcm.account.service.user.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import io.github.ntduycs.jhcm.account.domain.entity.User;
import io.github.ntduycs.jhcm.account.domain.repository.UserRepository;
import io.github.ntduycs.jhcm.account.service.user.UserMapper;
import io.github.ntduycs.jhcm.account.service.user.model.GetUserRequest;
import io.github.ntduycs.jhcm.account.service.user.model.GetUserResponse;
import io.github.ntduycs.jhcm.base.http.exception.HttpException;
import io.github.ntduycs.jhcm.base.http.exception.model.HttpError;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class GetUserUseCaseTest {
  @Mock private UserRepository userRepository;
  @Mock private UserMapper userMapper;

  @InjectMocks private GetUserUseCase getUserUseCase;

  @Test
  void shouldSuccess_givenValidRequest_whenUsersFound_thenReturnsUser() {
    when(userRepository.findByCode(anyString())).thenReturn(Optional.of(new User()));
    when(userMapper.fromUserToGetResponse(any(User.class)))
        .thenReturn(new GetUserResponse().setCode("1"));

    var result = getUserUseCase.handle(new GetUserRequest().setCode("1"));

    StepVerifier.create(result).expectNext(new GetUserResponse().setCode("1")).verifyComplete();

    verify(userRepository, times(1)).findByCode(anyString());
  }

  @Test
  void shouldFail_givenValidRequest_whenUsersNotFound_thenReturnsEmpty() {
    when(userRepository.findByCode(anyString())).thenReturn(Optional.empty());

    var result = getUserUseCase.handle(new GetUserRequest().setCode("1"));

    StepVerifier.create(result)
        .expectErrorSatisfies(
            ex -> {
              var appEx = (HttpException) ex;
              assertEquals(HttpError.NOT_FOUND, appEx.getError());
            })
        .verify();

    verify(userRepository, times(1)).findByCode(anyString());
  }
}
