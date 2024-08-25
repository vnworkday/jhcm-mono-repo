package io.github.ntduycs.jhcm.base.util;

import static com.alibaba.fastjson2.JSONWriter.Feature.PrettyFormat;
import static lombok.AccessLevel.PRIVATE;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class JsonUtils {

  public static String stringify(Object obj) {
    return JSON.toJSONString(obj);
  }

  public static String prettyStringify(Object obj) {
    return JSON.toJSONString(obj, PrettyFormat);
  }

  public static <T> T parse(String jsonStr, Class<T> objClass) {
    return JSON.parseObject(jsonStr, objClass);
  }

  /**
   * Parse json string to object with type reference. Example:
   *
   * <pre>
   *   var typeReference = new TypeReference<List<User>>() {};
   *   var users = JsonUtils.parse(jsonStr, typeReference);
   * </pre>
   */
  public static <T> T parse(String jsonStr, TypeReference<T> typeReference) {
    return JSON.parseObject(jsonStr, typeReference.getType());
  }
}
