package raisetech.student.management.exception;

public class InvalidAccessException extends StudentManagementException {

  public InvalidAccessException() {
    this.field = "URL";
    this.message = "現在無効なURLです。受講生一覧を見るには /studentList にアクセスしてください。";
  }

}
