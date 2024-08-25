package io.github.ntduycs.jhcm.account.service.user.usecase;

import static io.github.ntduycs.jhcm.base.http.exception.model.HttpError.NOT_FOUND;

import io.github.ntduycs.jhcm.account.domain.repository.UserRepository;
import io.github.ntduycs.jhcm.account.service.user.UserMapper;
import io.github.ntduycs.jhcm.account.service.user.model.GetUserRequest;
import io.github.ntduycs.jhcm.account.service.user.model.GetUserResponse;
import io.github.ntduycs.jhcm.base.http.exception.HttpException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetUserUseCase {
  private final UserRepository userRepository;

  private final UserMapper userMapper;

  public Mono<GetUserResponse> handle(GetUserRequest request) {
    return userRepository
        .findByCode(request.getCode())
        .map(userMapper::fromUserToGetResponse)
        .map(Mono::just)
        .orElseGet(() -> Mono.error(new HttpException(NOT_FOUND, "User not found")));
  }
}
