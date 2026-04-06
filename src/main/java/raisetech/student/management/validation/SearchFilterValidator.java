package raisetech.student.management.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import raisetech.student.management.search.request.SearchFilter;
import raisetech.student.management.search.request.SearchOperator;
import raisetech.student.management.search.request.SearchableField;

public class SearchFilterValidator implements ConstraintValidator<ValidSearchFilter, SearchFilter> {

  @Override
  public boolean isValid(SearchFilter filter, ConstraintValidatorContext context) {
    if (filter == null) {
      return true;
    }

    boolean isValid = true;

    String field = filter.getField();
    SearchOperator operator = filter.getOperator();

    // @NotNull 側で拾うので、ここでは深追いしない
    if (field == null ||field.equals("") || operator == null) {
      return true;
    }

    context.disableDefaultConstraintViolation();

    // 1. 演算子と value / values の整合性
    if (!validateOperatorAndValue(filter, context)) {
      isValid = false;
    }

    // 2. 有効なフィールド名かをチェック
    if(!SearchableField.existsSearchableFields(field)){
      addError(
          context,
          "field",
          field + " は無効なフィールド名です。対応している検索フィールド：" + String.join(",", SearchableField.getAllFieldNames())
      );
      isValid = false;

      // フィールド名が無効だと型が取得できないため、型整合性のチェックに進まずリターンする
      return isValid;
    }

    // 3. 型整合性
    Class<?> fieldType = SearchableField.getTypeByFieldName(field);
    if (!isTypeConsistent(filter, fieldType)) {
      addError(
          context,
          "value",
          fieldType.getSimpleName() + "型として正しくない形式です。"
      );
      isValid = false;
    }

    return isValid;
  }

  private boolean validateOperatorAndValue(SearchFilter filter, ConstraintValidatorContext context) {
    SearchOperator operator = filter.getOperator();
    String value = filter.getValue();
    List<String> values = filter.getValues();

    boolean hasValue = value != null && !value.isBlank();
    boolean hasValues = values != null && !values.isEmpty();
    int valuesSize = hasValues ? values.size() : 0;

    boolean isValid = true;

    switch (operator) {

      case BETWEEN -> {

        // values 未指定
        if (!hasValues) {
          addError(context, "values", "BETWEENではvaluesは2件で指定してください。");
          isValid = false;
          break;
        }

        // 要素数チェック
        if (valuesSize != 2) {
          addError(context, "values", "BETWEENではvaluesは2件で指定してください。");
          isValid = false;
        }

        // 同値チェック
        if (valuesSize == 2 && Objects.equals(values.get(0), values.get(1))) {
          addError(context, "values", "BETWEENでは異なる2つの値を指定してください。");
          isValid = false;
        }
      }

      case IN -> {

        // values 未指定
        if (!hasValues) {
          addError(context, "values", "INではvaluesで2件以上指定してください。");
          isValid = false;
          break;
        }

        // 件数チェック
        if (valuesSize < 2) {
          addError(context, "values", "INではvaluesで2件以上指定してください。");
          isValid = false;
        }
      }

      default -> {
        // value 必須
        if (!hasValue) {
          addError(context, "value", operator + "ではvalueの指定が必須です。valuesは使用できません。");
          isValid = false;
        }
      }
    }

    return isValid;
  }

  /**
   * フィールドの型と入力値（value/values）の整合性をチェックします。
   * @param filter 検索条件
   * @param type フィールドの型
   * @return 入力値がフィールドの求める型に変換可能ならtrue,不可能ならfalse
   */
  private boolean isTypeConsistent(SearchFilter filter, Class<?> type) {
    List<String> valuesToCheck = new ArrayList<>();
    SearchOperator operator = filter.getOperator();

    // 演算子に基づいて、チェックすべき値だけをピックアップする
    switch (operator) {
      case BETWEEN, IN -> {
        if (filter.getValues() != null) {
          filter.getValues().stream()
              .filter(v -> v != null && !v.isBlank())
              .forEach(valuesToCheck::add);
        }
      }
      default -> {
        if (filter.getValue() != null && !filter.getValue().isBlank()) {
          valuesToCheck.add(filter.getValue());
        }
      }
    }

    // 空の場合は(必須チェックは別で行っているため)型整合性としてはOKとする
    return valuesToCheck.stream().allMatch(v -> isParsable(v, type));
  }

  /**
   * 文字列が指定された型に変換可能か判定します。
   * @param input 入力値
   * @param type フィールドが期待する型
   * @return 変換可能ならtrue,不可能ならfalse
   */
  private boolean isParsable(String input, Class<?> type) {
    try {
      // String型なら何でもOK
      if (type == String.class) {
        return true;
      }

      // Integer型のチェック (age, statusIdなど)
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

      return false;
    } catch (Exception e) {
      // パースに失敗した場合は整合性なしと判断
      return false;
    }
  }

  private void addError(ConstraintValidatorContext context, String property, String message) {
    context.buildConstraintViolationWithTemplate(message)
        .addPropertyNode(property)
        .addConstraintViolation();
  }
}