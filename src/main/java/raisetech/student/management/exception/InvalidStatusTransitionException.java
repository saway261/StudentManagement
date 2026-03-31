package raisetech.student.management.exception;

import lombok.Getter;

@Getter
public class InvalidStatusTransitionException extends RuntimeException {

  private final Integer fromStatusId;
  private final Integer toStatusId;

  public InvalidStatusTransitionException(Integer fromStatusId,Integer toStatusId) {
    super("許可されていないステータス遷移です");
    this.fromStatusId = fromStatusId;
    this.toStatusId = toStatusId;
  }
}
