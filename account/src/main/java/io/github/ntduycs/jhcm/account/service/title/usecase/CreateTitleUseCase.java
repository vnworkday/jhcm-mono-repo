package io.github.ntduycs.jhcm.account.service.title.usecase;

import static io.github.ntduycs.jhcm.account.domain.enums.ResourceType.TITLE;

import io.github.ntduycs.jhcm.account.domain.repository.TitleRepository;
import io.github.ntduycs.jhcm.account.helper.ResourceCodeHelper;
import io.github.ntduycs.jhcm.account.service.title.TitleMapper;
import io.github.ntduycs.jhcm.account.service.title.model.CreateTitleRequest;
import io.github.ntduycs.jhcm.account.service.title.model.CreateTitleResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateTitleUseCase {
  private final TitleMapper mapper;

  private final TitleRepository repository;

  private final ResourceCodeHelper resourceCodeHelper;

  public Mono<CreateTitleResponse> handle(CreateTitleRequest request) {
    var title = mapper.fromCreateRequestToTitle(request);
    title.setCode(resourceCodeHelper.generate(TITLE));
    title.setCreatedBy(0); // TODO: set creator
    title.setUpdatedBy(0); // TODO: set updater

    repository.insert(title);

    var response = mapper.fromTitleToCreateResponse(title);

    return Mono.just(response);
  }
}
