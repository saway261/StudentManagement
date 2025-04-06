package raisetech.student.management.exception;

public class NotExistIdException extends Exception {

  public NotExistIdException() {
    super();
  }

  public NotExistIdException(String message) {
    super(message);
  }

  public NotExistIdException(String message, Throwable cause) {
    super(message, cause);
  }

  public NotExistIdException(Throwable cause) {
    super(cause);
  }

  protected NotExistIdException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
