package io.github.ntduycs.jhcm.account.controller;

import io.github.ntduycs.jhcm.account.service.title.TitleService;
import io.github.ntduycs.jhcm.account.service.title.model.GetTitleRequest;
import io.github.ntduycs.jhcm.account.service.title.model.GetTitleResponse;
import io.github.ntduycs.jhcm.account.service.title.model.ListTitleRequest;
import io.github.ntduycs.jhcm.account.service.title.model.ListTitleResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/titles")
@RequiredArgsConstructor
public class TitleController {
  private final TitleService titleService;

  @GetMapping("/{code}")
  public Mono<ResponseEntity<GetTitleResponse>> getTitle(@PathVariable String code) {
    var request = new GetTitleRequest().setCode(code);
    return titleService.get(request).map(ResponseEntity::ok);
  }

  @GetMapping
  public Mono<ResponseEntity<ListTitleResponse>> listTitles(@Valid ListTitleRequest request) {
    return titleService.list(request).map(ResponseEntity::ok);
  }
}
