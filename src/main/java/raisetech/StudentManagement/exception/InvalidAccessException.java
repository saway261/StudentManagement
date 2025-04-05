package raisetech.StudentManagement.exception;

public class InvalidAccessException extends Exception {

  public InvalidAccessException() {
    super();
  }

  public InvalidAccessException(String message) {
    super(message);
  }

  public InvalidAccessException(String message, Throwable cause) {
    super(message, cause);
  }

  public InvalidAccessException(Throwable cause) {
    super(cause);
  }

  protected InvalidAccessException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
