package io.github.ntduycs.jhcm.account.service.title.usecase;

import static io.github.ntduycs.jhcm.base.http.exception.model.HttpError.NOT_FOUND;

import io.github.ntduycs.jhcm.account.domain.repository.TitleRepository;
import io.github.ntduycs.jhcm.account.service.title.TitleMapper;
import io.github.ntduycs.jhcm.account.service.title.model.GetTitleRequest;
import io.github.ntduycs.jhcm.account.service.title.model.GetTitleResponse;
import io.github.ntduycs.jhcm.base.http.exception.HttpException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetTitleUseCase {
  private final TitleRepository titleRepository;

  private final TitleMapper titleMapper;

  public Mono<GetTitleResponse> handle(GetTitleRequest request) {
    return titleRepository
        .findByCode(request.getCode())
        .map(titleMapper::fromTitleToGetResponse)
        .map(Mono::just)
        .orElseGet(() -> Mono.error(new HttpException(NOT_FOUND, "Title not found")));
  }
}
