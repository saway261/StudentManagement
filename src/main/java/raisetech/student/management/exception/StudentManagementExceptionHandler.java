package raisetech.student.management.exception;

import jakarta.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * API実行時に発生した例外をハンドリングし、クライアントに例外発生個所とエラーメッセージを返します。
 */
@RestControllerAdvice
public class StudentManagementExceptionHandler {

  /**
   * 無効なURIへのアクセスが発生したことをクライアントに返します。
   *
   * @param ex InvalidAccessException
   * @return HTTPステータス(BAD REQUEST), エラー発生個所, エラーメッセージ
   */
  @ExceptionHandler(InvalidAccessException.class)
  public ResponseEntity<ErrorResponseBody> handleInvalidAccessException(InvalidAccessException ex) {

    ErrorResponseBody errorResponseBody =
        new ErrorResponseBody(HttpStatus.BAD_REQUEST, "page not exist", buildErrorDetails(ex));
    return ResponseEntity.badRequest().body(errorResponseBody);
  }

  /**
   * 入力されたIDが不正であることをクライアントに返します。
   *
   * @param ex InvalidIdException
   * @return HTTPステータス(BAD REQUEST), エラー発生個所, エラーメッセージ
   */
  @ExceptionHandler(InvalidIdException.class)
  public ResponseEntity<ErrorResponseBody> handleInvalidIdException(InvalidIdException ex) {

    ErrorResponseBody errorResponseBody =
        new ErrorResponseBody(HttpStatus.BAD_REQUEST, "invalid id", buildErrorDetails(ex));
    return ResponseEntity.badRequest().body(errorResponseBody);
  }

  /**
   * バリデーションエラーの発生をクライアントに返します。
   *
   * @param ex MethodArgumentNotValidException
   * @return HTTPステータス(BAD REQUEST), エラー発生個所, エラーメッセージ
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponseBody> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException ex) {

    ErrorResponseBody errorResponseBody =
        new ErrorResponseBody(HttpStatus.BAD_REQUEST, "validation error", buildErrorDetails(ex));
    return ResponseEntity.badRequest().body(errorResponseBody);
  }

  /**
   * バリデーションエラーの発生をクライアントに返します。
   *
   * @param ex ConstraintViolationException
   * @return HTTPステータス(BAD REQUEST), エラー発生個所, エラーメッセージ
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponseBody> handleConstraintViolationException(
      ConstraintViolationException ex) {

    ErrorResponseBody errorResponseBody = new ErrorResponseBody(HttpStatus.BAD_REQUEST,
        "validation error", buildErrorDetails(ex));
    return ResponseEntity.badRequest().body(errorResponseBody);
  }

  /**
   * MethodArgumentNotValidExceptionを受け取り、すべてのエラー発生個所とエラーメッセージをリストで返します。
   *
   * @param ex MethodArgumentNotValidException
   * @return エラー発生個所とエラーメッセージ
   */
  private List<Map<String, String>> buildErrorDetails(MethodArgumentNotValidException ex) {

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
  private List<Map<String, String>> buildErrorDetails(StudentManagementException ex) {

    List<Map<String, String>> errors = new ArrayList<>();
    Map<String, String> error = new HashMap<>();

    error.put("field", ex.getField());
    error.put("message", ex.getMessage());
    errors.add(error);

    return errors;
  }

  private List<Map<String, String>> buildErrorDetails(ConstraintViolationException ex) {

    List<Map<String, String>> errors = new ArrayList<>();
    Map<String, String> error = new HashMap<>();

    ex.getConstraintViolations().forEach(violation -> {
      error.put("field", violation.getPropertyPath().toString());
      error.put("message", violation.getMessage());
    });
    errors.add(error);
    return errors;
  }

}
