package raisetech.student.management.exception.handler;

import jakarta.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.TypeMismatchException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import raisetech.student.management.exception.InvalidSearchCriteriaException;
import raisetech.student.management.exception.InvalidStatusTransitionException;
import raisetech.student.management.exception.TargetNotFoundException;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JacksonException.Reference;
import tools.jackson.core.JsonToken;
import tools.jackson.databind.exc.InvalidFormatException;
import tools.jackson.databind.exc.MismatchedInputException;

@Component
public class ErrorDetailsBuilder {

  /**
   * MethodArgumentNotValidExceptionを受け取り、すべてのエラー発生個所とエラーメッセージをリストで返します。
   *
   * @param ex MethodArgumentNotValidException
   * @return エラー発生個所とエラーメッセージ
   */
  public List<Map<String, String>> buildErrorDetails(MethodArgumentNotValidException ex) {
    List<Map<String, String>> errors = new ArrayList<>();

    ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
      Map<String, String> error = new HashMap<>();
      error.put("field", fieldError.getField());
      error.put("message", fieldError.getDefaultMessage());
      errors.add(error);
    });
    return errors;
  }

  /**
   * ConstraintViolationExceptionを受け取り、すべてのエラー発生個所とエラーメッセージをリストで返します。
   *
   * @param ex ConstraintViolationException
   * @return エラー発生個所とエラーメッセージ
   */
  public List<Map<String, String>> buildErrorDetails(ConstraintViolationException ex) {
    List<Map<String, String>> errors = new ArrayList<>();

    ex.getConstraintViolations().forEach(violation -> {
      Map<String, String> error = new HashMap<>();
      error.put("field", violation.getPropertyPath().toString());
      error.put("message", violation.getMessage());
      errors.add(error);
    });
    return errors;
  }

  /**
   * TypeMismatchExceptionを受け取り、エラー発生個所とエラーメッセージをリストで返します。
   *
   * @param ex TypeMismatchException
   * @return エラー発生個所とエラーメッセージ
   */
  public List<Map<String, String>> buildErrorDetails(TypeMismatchException ex) {
    List<Map<String, String>> errors = new ArrayList<>();
    Map<String, String> error = new HashMap<>();

    String fieldName = ex.getPropertyName() != null ? ex.getPropertyName() : "unknown";
    String requiredType =
        ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown type";
    String message = String.format("値を %s 型に変換できませんでした", requiredType);

    error.put("field", fieldName);
    error.put("message", message);
    errors.add(error);
    return errors;
  }

  /**
   * TargetNotFoundExceptionを受け取り、エラー発生個所とエラーメッセージをリストで返します。
   *
   * @param ex TargetNotFoundException
   * @return エラー発生個所とエラーメッセージ
   */
  public List<Map<String, String>> buildErrorDetails(TargetNotFoundException ex) {
    List<Map<String, String>> errors = new ArrayList<>();

    Map<String, String> error = new HashMap<>();
    error.put("field", ex.getField());
    error.put("message", ex.getMessage());
    errors.add(error);

    return errors;
  }

  /**
   * InvalidStatusTransitionExceptionを受け取り、エラーメッセージと遷移前、遷移後のステータスIDを返します。
   *
   * @param ex InvalidStatusTransitionException
   * @return 遷移前と遷移後のステータスIDとエラーメッセージ
   */
  public List<Map<String, String>> buildErrorDetails(InvalidStatusTransitionException ex) {
    List<Map<String, String>> errors = new ArrayList<>();

    Map<String, String> error = new HashMap<>();
    error.put("value", "遷移前: "+ ex.getFromStatusId() + " -> 遷移後: " + ex.getToStatusId());
    error.put("message", ex.getMessage());
    errors.add(error);

    return errors;
  }

  /**
   * HttpMessageNotReadableExceptionの原因JacksonExceptionを受け取り、JSONパースエラー発生個所とエラーメッセージを返します。
   *
   * @param ex JacksonException
   * @return
   */
  public List<Map<String, String>> buildErrorDetails(JacksonException ex) {
    List<Map<String, String>> errors = new ArrayList<>();
    Map<String, String> error = new HashMap<>();

    switch (ex) {
      case InvalidFormatException ife -> {

        error.put("summary", "入力値の型が期待される型と異なります。");
        error.put("field",  formatFieldPath(ife.getPath()));
        error.put("actual input", "入力タイプ:" + getReadableTokenDescription(ife.getCurrentToken()) + ", 値:" + ife.getValue());
        error.put("expect type", ife.getTargetType().getSimpleName() + convertEnumToString(ife));
      }
      case MismatchedInputException mie -> {

        error.put("summary", "JSONの入力構造が一致しません。");
        error.put("field",formatFieldPath(mie.getPath()));
        error.put("expect type", mie.getTargetType().getTypeName());
        error.put("actual input", getReadableTokenDescription(mie.getCurrentToken()));
      }
      default -> {
        // JacksonException のその他の子クラスだった場合のデフォルト処理
        error.put("details", "JSONの解析に失敗しました。");
      }
    }
    errors.add(error);

    return errors;
  }

  /**
   * InvalidSearchCriteriaExceptionを受け取り、整合性違反が発生した検索対象フィールドとエラーの詳細を返します。
   *
   * @param ex InvalidSearchCriteriaException
   * @return 整合性違反が発生した検索対象フィールドとエラーの内容（関連があれば渡された演算子も含める）
   */
  public List<Map<String, String>> buildErrorDetails(InvalidSearchCriteriaException ex) {
    List<Map<String, String>> errors = new ArrayList<>();

    Map<String, String> error = new HashMap<>();
    error.put("field", ex.getField().getFieldName());

    if(ex.getOperator() != null){
      error.put("operator",ex.getOperator().toString());
    }
    error.put("message", ex.getMessage());
    errors.add(error);

    return errors;
  }

  /**
   * 例外インスタンスが持っている例外発生箇所のパスをAPI利用者が理解しやすい形に整形します。
   * @param path JacksonException#getPath()で取得したパス
   * @return パースエラーが発生したフィールド名
   */
  private String formatFieldPath(List<Reference> path) {
    StringBuilder builder = new StringBuilder();

    for (Reference ref : path) {

      String propName = ref.getPropertyName();

      // プロパティ名がない（＝インデックス）場合は、追加してすぐ次のループへ（早期コンティニュー）
      if (propName == null) {
        builder.append("[").append(ref.getIndex()).append("]");
        continue;
      }

      // ここから下はプロパティ名がある場合のみ実行される
      if (!builder.isEmpty()) { //最初の要素ならフィールド名の前にドットをつけない
        builder.append(".");
      }
      builder.append(propName);
    }

    return builder.toString();
  }

  /**
   * JsonTokenをAPI利用者が理解しやすいデータ型の文字列表現に変換します。(Jackson 3.x対応)
   *
   * @param token 実際に受け取ったJsonToken
   * @return わかりやすいデータ型の説明
   */
  private String getReadableTokenDescription(JsonToken token) {
    if (token == null) {
      return "入力の終わり (End of input)";
    }

    return switch (token) {
      case START_ARRAY, END_ARRAY -> "配列 (Array)";
      case START_OBJECT, END_OBJECT -> "オブジェクト (Object)";
      case PROPERTY_NAME -> "プロパティ名 (Property Name)"; // Jackson 3.x のトークン名
      case VALUE_STRING -> "文字列 (String)";
      case VALUE_NUMBER_INT -> "整数 (Integer)";
      case VALUE_NUMBER_FLOAT -> "小数 (Float)";
      case VALUE_TRUE, VALUE_FALSE -> "真偽値 (Boolean)";
      case VALUE_NULL -> "null";
      case VALUE_EMBEDDED_OBJECT -> "バイナリ等の埋め込みデータ (Embedded Object)";
      case NOT_AVAILABLE -> "利用不可 (Not Available)";
    };
  }

  private String convertEnumToString(MismatchedInputException ex){
    Class<?> targetType = ex.getTargetType();

    if(targetType.isEnum()){
      Object[] enums = targetType.getEnumConstants();

      if (enums != null) {
        String joinedValues = Stream.of(enums)
            .map(Object::toString)
            .collect(Collectors.joining(", "));
        return "  許可されている値：" + joinedValues;
      }
    }

    return "";
  }
}
