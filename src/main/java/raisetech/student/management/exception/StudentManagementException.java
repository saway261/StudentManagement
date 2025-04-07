package raisetech.student.management.exception;

import lombok.Getter;

@Getter
public abstract class StudentManagementException extends Exception {

  protected String field;
  protected String message;

}
