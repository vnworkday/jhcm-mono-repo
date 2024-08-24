package io.github.ntduycs.jhcm.base.logging;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class LogRedactionConfig extends PatternLayout {
  private Pattern multilinePattern;
  private final List<String> maskPatterns = new ArrayList<>();

  // invoked for every single entry in the xml
  public void addMaskPattern(String maskPattern) {
    maskPatterns.add(maskPattern);
    multilinePattern = Pattern.compile(String.join("|", maskPatterns), Pattern.MULTILINE);
  }

  @Override
  public String doLayout(ILoggingEvent event) {
    return maskMessage(super.doLayout(event));
  }

  private String maskMessage(String message) {
    if (multilinePattern == null) {
      return message;
    }

    var sb = new StringBuilder(message);
    var matcher = multilinePattern.matcher(sb);

    while (matcher.find()) {
      IntStream.rangeClosed(1, matcher.groupCount())
          .filter(group -> matcher.group(group) != null)
          .forEach(
              group ->
                  IntStream.range(matcher.start(group), matcher.end(group))
                      .forEach(i -> sb.setCharAt(i, '*')));
    }

    // return the masked substring with masked string of fixed 10 "*" characters
    return sb.toString().replaceAll("\\*{2,}", "********");
  }
}
