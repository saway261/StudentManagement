package raisetech.student.management.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.search.request.SearchFilter;
import raisetech.student.management.search.request.SearchOperator;

public class SearchFilterValidator implements ConstraintValidator<ValidSearchFilter, SearchFilter> {

  private Map<String, Class<?>> fieldTypeMap;

  @Override
  public void initialize(ValidSearchFilter constraintAnnotation) {
    fieldTypeMap = new HashMap<>();
    // StudentとStudentCourseの全フィールド名を型と共にキャッシュ
    Stream.of(Student.class, StudentCourse.class)
        .flatMap(clazz -> Stream.of(clazz.getDeclaredFields()))
        .forEach(f -> fieldTypeMap.put(f.getName(), f.getType()));
  }

  @Override
  public boolean isValid(SearchFilter filter, ConstraintValidatorContext context) {
    if (filter == null) return true;

    boolean isValid = true; // フラグを用意

    // 1. フィールド名の存在チェック
    if (!fieldTypeMap.containsKey(filter.getField())) {
      isValid = addError(context, "field", "指定されたフィールド名は存在しません。");
    }

    // 2. 演算子と値の整合性チェック
    if (!isValidOperator(filter)) {
      isValid = addError(context, "operator", "演算子に対して値(value/values)の指定が正しくありません。");
    }

    // 3. 型の整合性チェック (LocalDate等のパース)
    Class<?> targetType = fieldTypeMap.get(filter.getField());
    if (!isTypeConsistent(filter, targetType)) {
      isValid = addError(context, "value", targetType.getSimpleName() + "型として正しくない形式です。");
    }

    return isValid;
  }

  private boolean isValidOperator(SearchFilter filter) {
    SearchOperator operator = filter.getOperator();
    String value = filter.getValue();
    List<String> values = filter.getValues();

    if (operator == null) {
      return true;
    }

    boolean hasValue = (value != null && !value.isBlank());
    boolean hasValues = (values != null && !values.isEmpty());
    int valuesSize = hasValues ? values.size() : 0;

    return switch (operator) {
      case SearchOperator.BETWEEN ->
        // valuesが2つあり、かつそれらが同値でないことを確認
          !hasValue
              && valuesSize == 2
              && !Objects.equals(values.get(0), values.get(1));
      case SearchOperator.IN -> !hasValue && valuesSize >= 2;
      default -> hasValue && !hasValues;
    };
  }

  /**
   * フィールドの型と入力値（value/values）の整合性をチェックします。
   */
  private boolean isTypeConsistent(SearchFilter filter, Class<?> type) {
    // チェック対象の値をすべて集める（null以外）
    List<String> valuesToCheck = new ArrayList<>();
    if (filter.getValue() != null && !filter.getValue().isBlank()) {
      valuesToCheck.add(filter.getValue());
    }
    if (filter.getValues() != null) {
      filter.getValues().stream()
          .filter(v -> v != null && !v.isBlank())
          .forEach(valuesToCheck::add);
    }

    // 全ての値が指定された型にパースできるか確認
    return valuesToCheck.stream().allMatch(v -> isParsable(v, type));
  }

  /**
   * 文字列が指定された型に変換可能か判定します。
   */
  private boolean isParsable(String input, Class<?> type) {
    try {
      // String型なら何でもOK
      if (type == String.class) {
        return true;
      }

      // Integer型のチェック (age, statusId, studentIdなど)
      if (type == Integer.class) {
        Integer.parseInt(input);
        return true;
      }

      // LocalDate型のチェック (courseApplyAtなど)
      if (type == LocalDate.class) {
        // 標準的な ISO_LOCAL_DATE (yyyy-MM-dd) 形式を想定
        LocalDate.parse(input);
        return true;
      }

      // Boolean型のチェック (isDeleted)
      if (type == Boolean.class) {
        // parseBooleanは不正な文字列をすべてfalseにするため、明示的にチェック
        return input.equalsIgnoreCase("true") || input.equalsIgnoreCase("false");
      }

      return true;
    } catch (Exception e) {
      // パースに失敗した場合は整合性なしと判断
      return false;
    }
  }

  private boolean addError(ConstraintValidatorContext context, String property, String message) {
    context.disableDefaultConstraintViolation();
    context.buildConstraintViolationWithTemplate(message)
        .addPropertyNode(property)
        .addConstraintViolation();
    return false;
  }
}