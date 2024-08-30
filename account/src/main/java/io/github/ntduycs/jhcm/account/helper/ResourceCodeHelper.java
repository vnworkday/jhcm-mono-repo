package io.github.ntduycs.jhcm.account.helper;

import static java.nio.charset.StandardCharsets.UTF_8;

import io.github.ntduycs.jhcm.account.domain.enums.ResourceType;
import io.github.ntduycs.jhcm.account.domain.repository.SequenceRepository;
import java.util.ArrayList;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ResourceCodeHelper {
  private final SequenceRepository sequenceRepository;

  public String generate(ResourceType resource) {
    var sequence = sequenceRepository.getNextSequenceValue(resource.getCode());
    var source = resource.getCode() + sequence;
    var shuffled = shuffle(source);

    return toHexString(shuffled.getBytes(UTF_8));
  }

  private String shuffle(String str) {
    var chars = new ArrayList<Character>(str.length());
    for (char c : str.toCharArray()) {
      chars.add(c);
    }

    Collections.shuffle(chars);

    var shuffled = new StringBuilder(chars.size());
    for (char c : chars) {
      shuffled.append(c);
    }

    return shuffled.toString();
  }

  private String toHexString(byte[] bytes) {
    var hexString = new StringBuilder();
    for (byte b : bytes) {
      hexString.append(String.format("%02x", b));
    }
    return hexString.toString();
  }
}
