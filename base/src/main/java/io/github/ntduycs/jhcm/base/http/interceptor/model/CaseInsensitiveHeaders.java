package io.github.ntduycs.jhcm.base.http.interceptor.model;

import java.util.*;
import java.util.stream.Collectors;
import org.springframework.lang.NonNull;
import org.springframework.util.MultiValueMap;

public class CaseInsensitiveHeaders implements Map<String, String> {
  private final Map<String, String> headers = new LinkedHashMap<>();

  private CaseInsensitiveHeaders(MultiValueMap<String, String> headers) {
    for (var entry : headers.entrySet()) {
      var k = toUpperKebabKey(entry.getKey());
      var v = String.join(", ", entry.getValue());
      this.headers.put(k, v);
    }
  }

  private CaseInsensitiveHeaders(Iterable<Map.Entry<String, String>> headers) {
    for (var entry : headers) {
      var k = toUpperKebabKey(entry.getKey());
      var v = entry.getValue();
      this.headers.put(k, v);
    }
  }

  public static CaseInsensitiveHeaders from(MultiValueMap<String, String> headers) {
    return new CaseInsensitiveHeaders(headers);
  }

  public static CaseInsensitiveHeaders from(Iterable<Map.Entry<String, String>> headers) {
    return new CaseInsensitiveHeaders(headers);
  }

  @Override
  public int size() {
    return headers.size();
  }

  @Override
  public boolean isEmpty() {
    return headers.isEmpty();
  }

  @Override
  public boolean containsKey(Object key) {
    return headers.containsKey(toUpperKebabKey((String) key));
  }

  @Override
  public boolean containsValue(Object value) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String get(Object key) {
    return headers.get(toUpperKebabKey((String) key));
  }

  @Override
  public String put(String key, String value) {
    if (key == null) {
      throw new NullPointerException();
    }

    var k = toUpperKebabKey(key);
    var v = headers.getOrDefault(k, "") + ", " + value;

    return headers.put(k, v);
  }

  @Override
  public String remove(Object key) {
    return headers.remove(toUpperKebabKey((String) key));
  }

  @Override
  public void putAll(Map<? extends String, ? extends String> m) {
    for (var entry : m.entrySet()) {
      put(entry.getKey(), entry.getValue());
    }
  }

  @Override
  public void clear() {
    headers.clear();
  }

  @Override
  @NonNull public Set<String> keySet() {
    return headers.keySet();
  }

  @Override
  @NonNull public Collection<String> values() {
    return headers.values();
  }

  @Override
  @NonNull public Set<Entry<String, String>> entrySet() {
    return headers.entrySet();
  }

  private String toUpperKebabKey(String key) {
    var k =
        key.replace("_", "-") // Replace underscores with hyphens
            .replaceAll("-+", "-"); // Remove consecutive hyphens

    return Arrays.stream(k.split("-"))
        .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
        .collect(Collectors.joining("-"));
  }
}
