package raisetech.student.management.exception.handler;

import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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

    ErrorResponse ErrorResponse =
        new ErrorResponse(HttpStatus.BAD_REQUEST, "validation error",
            errorDetailsBuilder.buildErrorDetails(ex));
    return ResponseEntity.badRequest().body(ErrorResponse);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
      MethodArgumentTypeMismatchException ex) {

    ErrorResponse ErrorResponse =
        new ErrorResponse(HttpStatus.BAD_REQUEST, "validation error",
            errorDetailsBuilder.buildErrorDetails(ex));
    return ResponseEntity.badRequest().body(ErrorResponse);
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

    ErrorResponse ErrorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST,
        "validation error", errorDetailsBuilder.buildErrorDetails(ex));
    return ResponseEntity.badRequest().body(ErrorResponse);
  }
}
