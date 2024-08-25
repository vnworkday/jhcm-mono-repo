package io.github.ntduycs.jhcm.account.service.user.usecase;

import io.github.ntduycs.jhcm.account.domain.repository.UserRepository;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SyncUserUseCase {
  private final UserRepository userRepository;

  private final Random random = new Random();

  public void sync() throws InterruptedException {
    log.info("Syncing users...");

    if (random.nextInt(3) == 1) {
      throw new RuntimeException("Random error occurred while syncing users");
    }

    Thread.sleep(2000);

    log.info("Users synced successfully");
  }
}
