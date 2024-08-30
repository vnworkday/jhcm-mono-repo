package io.github.ntduycs.jhcm.account.service.title;

import io.github.ntduycs.jhcm.account.service.title.model.*;
import io.github.ntduycs.jhcm.account.service.title.usecase.CreateTitleUseCase;
import io.github.ntduycs.jhcm.account.service.title.usecase.GetTitleUseCase;
import io.github.ntduycs.jhcm.account.service.title.usecase.ListTitleUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TitleService {
  private final GetTitleUseCase getTitleUseCase;
  private final ListTitleUseCase listTitleUseCase;
  private final CreateTitleUseCase createTitleUseCase;

  public Mono<ListTitleResponse> list(ListTitleRequest request) {
    return listTitleUseCase.handle(request);
  }

  public Mono<GetTitleResponse> get(GetTitleRequest request) {
    return getTitleUseCase.handle(request);
  }

  public Mono<CreateTitleResponse> create(CreateTitleRequest request) {
    return createTitleUseCase.handle(request);
  }
}
