package io.github.ntduycs.jhcm.account.service.title.usecase;

import static io.github.ntduycs.jhcm.base.http.HttpUtils.calculateTotalPages;

import io.github.ntduycs.jhcm.account.domain.repository.TitleRepository;
import io.github.ntduycs.jhcm.account.service.title.TitleMapper;
import io.github.ntduycs.jhcm.account.service.title.model.ListTitleRequest;
import io.github.ntduycs.jhcm.account.service.title.model.ListTitleResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class ListTitleUseCase {
  private final TitleRepository titleRepository;

  private final TitleMapper titleMapper;

  public Mono<ListTitleResponse> handle(ListTitleRequest request) {
    return Mono.zip(
            Mono.fromCallable(() -> titleRepository.search(request))
                .flatMap(
                    titles ->
                        Mono.just(
                            titles.stream().map(titleMapper::fromTitleToListResponse).toList())),
            Mono.fromCallable(() -> titleRepository.count(request)))
        .map(
            tuple -> {
              var titles = tuple.getT1();
              var count = tuple.getT2();
              return new ListTitleResponse()
                  .setItems(titles)
                  .setOffset(request.getOffset())
                  .setSize(request.getSize())
                  .setTotal(count)
                  .setTotalPages(calculateTotalPages(count, request.getSize()));
            });
  }
}
