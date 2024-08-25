package io.github.ntduycs.jhcm.{{service}}.controller;

import lombok.RequiredArgsConstructor;
import io.github.ntduycs.jhcm.{{service}}.service.{{module}}.{{Module}}Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/{{module}}s")
@RequiredArgsConstructor
public class {{Module}}Controller {
  private final {{Module}}Service service;

  // TODO: Fill your code here
}