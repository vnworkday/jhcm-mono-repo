package io.github.ntduycs.jhcm.account.service.user;

import io.github.ntduycs.jhcm.account.service.user.model.GetUserRequest;
import io.github.ntduycs.jhcm.account.service.user.model.GetUserResponse;
import io.github.ntduycs.jhcm.account.service.user.model.ListUserRequest;
import io.github.ntduycs.jhcm.account.service.user.model.ListUserResponse;
import io.github.ntduycs.jhcm.account.service.user.usecase.GetUserUseCase;
import io.github.ntduycs.jhcm.account.service.user.usecase.ListUserUseCase;
import io.github.ntduycs.jhcm.account.service.user.usecase.SyncUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {
  private final GetUserUseCase getUserUseCase;
  private final ListUserUseCase listUserUseCase;
  private final SyncUserUseCase syncUserUseCase;

  public Mono<GetUserResponse> get(GetUserRequest request) {
    return getUserUseCase.handle(request);
  }

  public Mono<ListUserResponse> list(ListUserRequest request) {
    return listUserUseCase.handle(request);
  }

  public void sync() throws InterruptedException {
    syncUserUseCase.sync();
  }
}
