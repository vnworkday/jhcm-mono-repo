package io.github.ntduycs.jhcm.account.controller;

import io.github.ntduycs.jhcm.account.service.user.UserService;
import io.github.ntduycs.jhcm.account.service.user.model.GetUserRequest;
import io.github.ntduycs.jhcm.account.service.user.model.GetUserResponse;
import io.github.ntduycs.jhcm.account.service.user.model.ListUserRequest;
import io.github.ntduycs.jhcm.account.service.user.model.ListUserResponse;
import io.github.ntduycs.jhcm.base.http.exception.model.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User operations")
public class UserController {
  private final UserService userService;

  @GetMapping("/{code}")
  @Operation(
      summary = "Get user by code",
      description = "Get user by code",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "User found",
            content = @Content(schema = @Schema(implementation = GetUserResponse.class))),
        @ApiResponse(
            responseCode = "404",
            description = "User not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
      })
  public Mono<ResponseEntity<GetUserResponse>> getUser(@PathVariable String code) {
    var request = new GetUserRequest().setCode(code);
    return userService.get(request).map(ResponseEntity::ok);
  }

  @GetMapping
  @Operation(
      summary = "List users",
      description = "List users with pagination",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "List of users",
            content = @Content(schema = @Schema(implementation = ListUserResponse.class)))
      })
  public Mono<ResponseEntity<ListUserResponse>> listUsers(
      @Valid @ParameterObject ListUserRequest request) {
    return userService.list(request).map(ResponseEntity::ok);
  }
}
