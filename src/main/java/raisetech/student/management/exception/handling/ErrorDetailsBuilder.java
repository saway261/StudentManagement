package raisetech.student.management.exception.handling;

import jakarta.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.TypeMismatchException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import raisetech.student.management.exception.StudentException;

/**
 * StudentManagementExceptionHandlerクラスから例外インスタンスを受け取り、エラーの詳細情報を組み立てて返します。
 */
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
   * カスタム例外クラスの例外を受け取り、エラー発生個所とエラーメッセージをリスト形式で返します。
   *
   * @param ex StudentManagementExceptionを継承したカスタム例外
   * @return エラー発生個所とエラーメッセージ
   */
  public List<Map<String, String>> buildErrorDetails(StudentException ex) {

    List<Map<String, String>> errors = new ArrayList<>();
    Map<String, String> error = new HashMap<>();

    error.put("field", ex.getField());
    error.put("message", ex.getMessage());
    errors.add(error);

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

}
