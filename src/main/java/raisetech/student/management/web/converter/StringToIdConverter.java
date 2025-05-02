package raisetech.student.management.web.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import raisetech.student.management.data.Id;

@Component
public class StringToIdConverter implements Converter<String, Id> {

  @Override
  public Id convert(String source) {
    if (source == null || source.isBlank()) {
      return null;
    }
    try {
      Integer intValue = Integer.valueOf(source);
      return new Id(intValue); // ← 1未満ならIdクラス内で例外が発生する
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("IDは整数値で指定してください: " + source, e);
    }
  }
}

