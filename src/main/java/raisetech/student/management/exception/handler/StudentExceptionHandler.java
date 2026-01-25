package raisetech.student.management.exception.handler;

import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import raisetech.student.management.exception.UpdateTargetNotFoundException;

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
        new ErrorResponse(HttpStatus.BAD_REQUEST, "validation error",
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
        new ErrorResponse(HttpStatus.BAD_REQUEST, "validation error",
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
        "validation error", errorDetailsBuilder.buildErrorDetails(ex));
    return ResponseEntity.badRequest().body(errorResponse);
  }

  /**
   * 更新処理においてstudentId,courseIdで指定するレコードが見つからなかったこと（null含む）をクライアントに返します。
   *
   * @param ex UpdateTargetNotFoundException
   * @return HTTPステータス(BAD REQUEST), エラー詳細
   */
  @ExceptionHandler(UpdateTargetNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleUpdateTargetNotFoundException(
      UpdateTargetNotFoundException ex) {

    ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST,
        "validation error", errorDetailsBuilder.buildErrorDetails(ex));
    return ResponseEntity.badRequest().body(errorResponse);
  }
}
