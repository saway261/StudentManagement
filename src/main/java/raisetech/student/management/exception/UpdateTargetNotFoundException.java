package raisetech.student.management.exception;

import lombok.Getter;

@Getter
public class UpdateTargetNotFoundException extends RuntimeException {

  private String field;

  public UpdateTargetNotFoundException(String field) {
    super("更新対象のインスタンスが見つかりませんでした");
    this.field = field;
  }
}
