package io.github.ntduycs.jhcm.account.service.user.usecase;

import io.github.ntduycs.jhcm.account.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SyncUserUseCase {
  private final UserRepository userRepository;

  public void sync() throws InterruptedException {
    log.info("Syncing users...");

    log.info("Users synced successfully");
  }
}
