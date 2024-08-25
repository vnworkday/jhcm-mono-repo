package io.github.ntduycs.jhcm.{{service}}.service.{{module}}.usecase;

import io.github.ntduycs.jhcm.{{service}}.service.{{module}}.model.{{Usecase}}Request;
import io.github.ntduycs.jhcm.{{service}}.service.{{module}}.model.{{Usecase}}Response;
import io.github.ntduycs.jhcm.{{service}}.service.{{module}}.{{Module}}Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class {{Usecase}}UseCase {
  private final {{Module}}Mapper mapper;

  public Mono<{{Usecase}}Response> handle({{Usecase}}Request request) {
    return Mono.just(new {{Usecase}}Response());
  }
}
