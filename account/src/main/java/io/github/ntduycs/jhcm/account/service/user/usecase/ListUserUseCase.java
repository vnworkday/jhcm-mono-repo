package io.github.ntduycs.jhcm.account.service.user.usecase;

import static io.github.ntduycs.jhcm.base.http.HttpUtils.calculateTotalPages;

import io.github.ntduycs.jhcm.account.domain.repository.UserRepository;
import io.github.ntduycs.jhcm.account.service.user.UserMapper;
import io.github.ntduycs.jhcm.account.service.user.model.ListUserRequest;
import io.github.ntduycs.jhcm.account.service.user.model.ListUserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class ListUserUseCase {
  private final UserRepository userRepository;

  private final UserMapper userMapper;

  public Mono<ListUserResponse> handle(ListUserRequest request) {
    return Mono.zip(
            Mono.fromCallable(() -> userRepository.search(request))
                .flatMap(
                    users ->
                        Mono.just(users.stream().map(userMapper::fromUserToListResponse).toList())),
            Mono.fromCallable(() -> userRepository.count(request)))
        .map(
            tuple -> {
              var users = tuple.getT1();
              var count = tuple.getT2();
              return new ListUserResponse()
                  .setItems(users)
                  .setOffset(request.getOffset())
                  .setSize(request.getSize())
                  .setTotal(count)
                  .setTotalPages(calculateTotalPages(count, request.getSize()));
            });
  }
}
