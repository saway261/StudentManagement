package raisetech.student.management.exception;

import jakarta.validation.ConstraintViolation;
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

@RestControllerAdvice
public class StudentManagementExceptionHandler {

  @ExceptionHandler(InvalidAccessException.class)
  public ResponseEntity<ErrorResponseBody> handleInvalidAccessException(InvalidAccessException ex) {

    ErrorResponseBody errorResponseBody =
        new ErrorResponseBody(HttpStatus.BAD_REQUEST, "page not exist", newErrorResponse(ex));
    return ResponseEntity.badRequest().body(errorResponseBody);
  }

  @ExceptionHandler(InvalidIdException.class)
  public ResponseEntity<ErrorResponseBody> handleInvalidIdException(InvalidIdException ex) {

    ErrorResponseBody errorResponseBody =
        new ErrorResponseBody(HttpStatus.BAD_REQUEST, "invalid id", newErrorResponse(ex));
    return ResponseEntity.badRequest().body(errorResponseBody);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponseBody> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException ex) {

    ErrorResponseBody errorResponseBody =
        new ErrorResponseBody(HttpStatus.BAD_REQUEST, "validation error", newErrorResponse(ex));
    return ResponseEntity.badRequest().body(errorResponseBody);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponseBody> handleConstraintViolationException(
      ConstraintViolationException ex) {

    ErrorResponseBody errorResponseBody = new ErrorResponseBody(HttpStatus.BAD_REQUEST,
        "validation error", newErrorResponse(ex));
    return ResponseEntity.badRequest().body(errorResponseBody);

  }

  private List<Map<String, String>> newErrorResponse(MethodArgumentNotValidException ex) {

    List<Map<String, String>> errors = new ArrayList<>();
    ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
      Map<String, String> error = new HashMap<>();
      error.put("field", fieldError.getField());
      error.put("message", fieldError.getDefaultMessage());
      errors.add(error);
    });
    return errors;
  }

  private List<Map<String, String>> newErrorResponse(StudentManagementException ex) {

    List<Map<String, String>> errors = new ArrayList<>();
    Map<String, String> error = new HashMap<>();

    error.put("field", ex.getField());
    error.put("message", ex.getMessage());
    errors.add(error);

    return errors;
  }

  private List<Map<String, String>> newErrorResponse(ConstraintViolationException ex) {

    List<Map<String, String>> errors = new ArrayList<>();
    Map<String, String> error = new HashMap<>();

    for (ConstraintViolation violation : ex.getConstraintViolations()) {
      error.put("field", violation.getPropertyPath().toString());
      error.put("message", violation.getMessage());
    }
    errors.add(error);

    return errors;
  }

}
