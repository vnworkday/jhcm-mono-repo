package io.github.ntduycs.jhcm.account.controller;

import io.github.ntduycs.jhcm.account.service.title.TitleService;
import io.github.ntduycs.jhcm.account.service.title.model.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("/titles")
@RequiredArgsConstructor
public class TitleController {
  private final TitleService titleService;

  @Value("${http.base-url}")
  private String baseUrl;

  @GetMapping("/{code}")
  public Mono<ResponseEntity<GetTitleResponse>> getTitle(@PathVariable String code) {
    var request = new GetTitleRequest().setCode(code);
    return titleService.get(request).map(ResponseEntity::ok);
  }

  @GetMapping
  public Mono<ResponseEntity<ListTitleResponse>> listTitles(@Valid ListTitleRequest request) {
    return titleService.list(request).map(ResponseEntity::ok);
  }

  @PostMapping
  public Mono<ResponseEntity<CreateTitleResponse>> createTitle(
      @Valid @RequestBody CreateTitleRequest request) {
    return titleService.create(request)
      .map(response -> ResponseEntity
        .created(URI.create(baseUrl + "/titles/" + response.getCode()))
        .body(response));
  }
}
