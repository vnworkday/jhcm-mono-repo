package io.github.ntduycs.jhcm.account.service.title;

import io.github.ntduycs.jhcm.account.service.title.model.GetTitleRequest;
import io.github.ntduycs.jhcm.account.service.title.model.GetTitleResponse;
import io.github.ntduycs.jhcm.account.service.title.model.ListTitleRequest;
import io.github.ntduycs.jhcm.account.service.title.model.ListTitleResponse;
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

  public Mono<ListTitleResponse> list(ListTitleRequest request) {
    return listTitleUseCase.handle(request);
  }

  public Mono<GetTitleResponse> get(GetTitleRequest request) {
    return getTitleUseCase.handle(request);
  }
}
