package io.github.ntduycs.jhcm.base.util;

import static lombok.AccessLevel.PRIVATE;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

@NoArgsConstructor(access = PRIVATE)
@Slf4j
public final class ClasspathUtils {

  public static Optional<String> getResourceAsString(String path) {
    try {
      var content = new ClassPathResource(path).getContentAsString(StandardCharsets.UTF_8);
      return Optional.of(content);
    } catch (FileNotFoundException e) {
      log.error("File not found: {}", path);
      return Optional.empty();
    } catch (IOException e) {
      log.error("Error reading file: {}", path);
      return Optional.empty();
    }
  }
}
