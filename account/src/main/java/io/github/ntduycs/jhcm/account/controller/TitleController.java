package io.github.ntduycs.jhcm.account.controller;

import io.github.ntduycs.jhcm.account.service.title.TitleService;
import io.github.ntduycs.jhcm.account.service.title.model.*;
import io.github.ntduycs.jhcm.base.http.exception.model.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/titles")
@RequiredArgsConstructor
@Tag(name = "Titles", description = "Title operations")
public class TitleController {
  private final TitleService titleService;

  @GetMapping("/{code}")
  @Operation(
      summary = "Get title by code",
      description = "Get title by code",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Title found",
            content = @Content(schema = @Schema(implementation = GetTitleResponse.class))),
        @ApiResponse(
            responseCode = "404",
            description = "Title not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
      })
  public Mono<ResponseEntity<GetTitleResponse>> getTitle(@PathVariable String code) {
    var request = new GetTitleRequest().setCode(code);
    return titleService.get(request).map(ResponseEntity::ok);
  }

  @GetMapping
  @Operation(
      summary = "List titles",
      description = "List titles with pagination",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "List of titles",
            content = @Content(schema = @Schema(implementation = ListTitleResponse.class)))
      })
  public Mono<ResponseEntity<ListTitleResponse>> listTitles(
      @Valid @ParameterObject ListTitleRequest request) {
    return titleService.list(request).map(ResponseEntity::ok);
  }

  @PostMapping
  @Operation(
      summary = "Create title",
      description = "Create title",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Title created",
            content = @Content(schema = @Schema(implementation = CreateTitleResponse.class))),
        @ApiResponse(
            responseCode = "409",
            description = "Title already exists with the same name",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
      })
  public Mono<ResponseEntity<CreateTitleResponse>> createTitle(
      @Valid @RequestBody CreateTitleRequest request) {
    return titleService.create(request).map(ResponseEntity::ok);
  }
}
