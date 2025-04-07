package raisetech.student.management.exception;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class StudentManagementExceptionHandler {

  @ExceptionHandler(InvalidAccessException.class)
  public ResponseEntity<String> handleInvalidAccessException(InvalidAccessException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }

  @ExceptionHandler(NotExistIdException.class)
  public ResponseEntity<String> handleNotExistIdException(NotExistIdException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponseBody> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException ex) {

    ErrorResponseBody errorResponseBody =
        new ErrorResponseBody(HttpStatus.BAD_REQUEST, "validation error", newErrorResponse(ex));
    return ResponseEntity.badRequest().body(errorResponseBody);
  }
  
  private List<Map<String, String>> newErrorResponse(BindException ex) {

    List<Map<String, String>> errors = new ArrayList<>();
    ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
      Map<String, String> error = new HashMap<>();
      error.put("field", fieldError.getField());
      error.put("message", fieldError.getDefaultMessage());
      errors.add(error);
    });
    return errors;
  }

}
