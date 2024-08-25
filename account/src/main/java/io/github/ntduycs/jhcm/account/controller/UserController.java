package io.github.ntduycs.jhcm.account.controller;

import io.github.ntduycs.jhcm.account.service.user.UserService;
import io.github.ntduycs.jhcm.account.service.user.model.GetUserRequest;
import io.github.ntduycs.jhcm.account.service.user.model.GetUserResponse;
import io.github.ntduycs.jhcm.account.service.user.model.ListUserRequest;
import io.github.ntduycs.jhcm.account.service.user.model.ListUserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @GetMapping("/{code}")
  public Mono<ResponseEntity<GetUserResponse>> getUser(@PathVariable String code) {
    var request = new GetUserRequest().setCode(code);
    return userService.get(request).map(ResponseEntity::ok);
  }

  @GetMapping
  public Mono<ResponseEntity<ListUserResponse>> listUsers(@Valid ListUserRequest request) {
    return userService.list(request).map(ResponseEntity::ok);
  }
}
