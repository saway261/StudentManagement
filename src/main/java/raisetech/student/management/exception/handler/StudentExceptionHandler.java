package raisetech.student.management.exception.handler;

import tools.jackson.core.JacksonException;
import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import raisetech.student.management.exception.InvalidStatusTransitionException;
import raisetech.student.management.exception.TargetNotFoundException;

@RestControllerAdvice
public class StudentExceptionHandler {

  ErrorDetailsBuilder errorDetailsBuilder;

  @Autowired
  public StudentExceptionHandler(ErrorDetailsBuilder errorDetailsBuilder) {
    this.errorDetailsBuilder = errorDetailsBuilder;
  }

  /**
   * バリデーションエラーの発生をクライアントに返します。
   *
   * @param ex MethodArgumentNotValidException
   * @return HTTPステータス(BAD REQUEST), エラー詳細
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException ex) {

    ErrorResponse errorResponse =
        new ErrorResponse(HttpStatus.BAD_REQUEST, "payload validation error",
            errorDetailsBuilder.buildErrorDetails(ex));
    return ResponseEntity.badRequest().body(errorResponse);
  }

  /**
   * バリデーションエラー(引数の型不一致)の発生をクライアントに返します。
   *
   * @param ex MethodArgumentTypeMismatchException
   * @return HTTPステータス(BAD REQUEST), エラー詳細
   */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
      MethodArgumentTypeMismatchException ex) {

    ErrorResponse errorResponse =
        new ErrorResponse(HttpStatus.BAD_REQUEST, "type mismatch error",
            errorDetailsBuilder.buildErrorDetails(ex));
    return ResponseEntity.badRequest().body(errorResponse);
  }

  /**
   * バリデーションエラーの発生をクライアントに返します。
   *
   * @param ex ConstraintViolationException
   * @return HTTPステータス(BAD REQUEST), エラー詳細
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleConstraintViolationException(
      ConstraintViolationException ex) {

    ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST,
        "parameter validation error", errorDetailsBuilder.buildErrorDetails(ex));
    return ResponseEntity.badRequest().body(errorResponse);
  }

  /**
   * 検索及び更新処理において studentId, studentCourseId, courseCode 等の識別子で指定するレコードが見つからなかったこと（nullだった場合を含む）をクライアントに返します。
   *
   * @param ex TargetNotFoundException
   * @return HTTPステータス(NOT_FOUND), エラー詳細
   */
  @ExceptionHandler(TargetNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleUpdateTargetNotFoundException(
      TargetNotFoundException ex) {

    ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND,
        "target not found", errorDetailsBuilder.buildErrorDetails(ex));
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);

  }

  /**
   * 受講生コースの現在のステータスと更新後のステータスを照合して、許可された遷移でなかった場合にユーザに詳細を伝えます。
   *
   * @param ex InvalidStatusTransitionException
   * @return HTTPステータス(BAD_REQUEST), エラー詳細
   */
  @ExceptionHandler(InvalidStatusTransitionException.class)
  public ResponseEntity<ErrorResponse> handleInvalidStatusTransitionException(
      InvalidStatusTransitionException ex) {

    ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST,
        "invalid status transition", errorDetailsBuilder.buildErrorDetails(ex));
    return ResponseEntity.badRequest().body(errorResponse);

  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
      HttpMessageNotReadableException ex){

    String message = ex.getMessage();
    Throwable cause = ex.getCause();

    // causeがnullなら自動でfalse
    if(cause instanceof JacksonException jpex){
      ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST,
          "json parse error", errorDetailsBuilder.buildErrorDetails(jpex));
      return ResponseEntity.badRequest().body(errorResponse);

    }else if(message.startsWith("Required request body is missing")){

      Map<String, String> error = new HashMap<>();
      error.put("field","requestBody");
      error.put("message","リクエストボディは必須です");

      ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST,
          "request body is missing", List.of(error));
      return ResponseEntity.badRequest().body(errorResponse);

    }else{

      Map<String, String> error = new HashMap<>();
      error.put("message","HTTPリクエストを変換できませんでした");

      ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST,
          "http message convert error",List.of(error));
      return ResponseEntity.badRequest().body(errorResponse);
    }
  }
}
