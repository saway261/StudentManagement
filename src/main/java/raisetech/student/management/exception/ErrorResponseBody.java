package raisetech.student.management.exception;

import java.util.List;
import java.util.Map;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public final class ErrorResponseBody {

  private final HttpStatus status;
  private final String message;
  private final List<Map<String, String>> errors;

  public ErrorResponseBody(HttpStatus status, String message, List<Map<String, String>> errors) {
    this.status = status;
    this.message = message;
    this.errors = errors;
  }

}
