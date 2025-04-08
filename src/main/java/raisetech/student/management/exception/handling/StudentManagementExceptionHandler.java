package raisetech.student.management.exception.handling;

import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import raisetech.student.management.exception.InvalidAccessException;
import raisetech.student.management.exception.InvalidIdException;

/**
 * API実行時に発生した例外をハンドリングし、クライアントに例外発生個所とエラーメッセージを返します。
 */
@RestControllerAdvice
public class StudentManagementExceptionHandler {

  ErrorDetailsBuilder errorDetailsBuilder;

  @Autowired
  public StudentManagementExceptionHandler(ErrorDetailsBuilder errorDetailsBuilder) {
    this.errorDetailsBuilder = errorDetailsBuilder;
  }

  /**
   * 無効なURIへのアクセスが発生したことをクライアントに返します。
   *
   * @param ex InvalidAccessException
   * @return HTTPステータス(NOT FOUND), エラー詳細
   */
  @ExceptionHandler(InvalidAccessException.class)
  public ResponseEntity<ErrorResponseBody> handleInvalidAccessException(InvalidAccessException ex) {

    ErrorResponseBody errorResponseBody =
        new ErrorResponseBody(HttpStatus.NOT_FOUND, "page not exist",
            errorDetailsBuilder.buildErrorDetails(ex));
    return ResponseEntity.status(404).body(errorResponseBody);
  }

  /**
   * 入力されたIDが不正であることをクライアントに返します。
   *
   * @param ex InvalidIdException
   * @return HTTPステータス(NOT FOUND), エラー詳細
   */
  @ExceptionHandler(InvalidIdException.class)
  public ResponseEntity<ErrorResponseBody> handleInvalidIdException(InvalidIdException ex) {

    ErrorResponseBody errorResponseBody =
        new ErrorResponseBody(HttpStatus.NOT_FOUND, "invalid id",
            errorDetailsBuilder.buildErrorDetails(ex));
    return ResponseEntity.status(404).body(errorResponseBody);
  }

  /**
   * バリデーションエラーの発生をクライアントに返します。
   *
   * @param ex MethodArgumentNotValidException
   * @return HTTPステータス(BAD REQUEST), エラー詳細
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponseBody> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException ex) {

    ErrorResponseBody errorResponseBody =
        new ErrorResponseBody(HttpStatus.BAD_REQUEST, "validation error",
            errorDetailsBuilder.buildErrorDetails(ex));
    return ResponseEntity.badRequest().body(errorResponseBody);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponseBody> handleMethodArgumentTypeMismatchException(
      MethodArgumentTypeMismatchException ex) {
    
    ErrorResponseBody errorResponseBody =
        new ErrorResponseBody(HttpStatus.BAD_REQUEST, "validation error",
            errorDetailsBuilder.buildErrorDetails(ex));
    return ResponseEntity.badRequest().body(errorResponseBody);
  }

  /**
   * バリデーションエラーの発生をクライアントに返します。
   *
   * @param ex ConstraintViolationException
   * @return HTTPステータス(BAD REQUEST), エラー詳細
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponseBody> handleConstraintViolationException(
      ConstraintViolationException ex) {

    ErrorResponseBody errorResponseBody = new ErrorResponseBody(HttpStatus.BAD_REQUEST,
        "validation error", errorDetailsBuilder.buildErrorDetails(ex));
    return ResponseEntity.badRequest().body(errorResponseBody);
  }


}
