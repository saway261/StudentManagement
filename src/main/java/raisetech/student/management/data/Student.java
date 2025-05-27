package raisetech.student.management.data;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import raisetech.student.management.data.value.Id;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class Student {

  private final Id studentId;

  private final String fullname;

  private final String kanaName;

  private final String nickname;

  private final String email;

  private final String area;

  private final String telephone;

  private final Integer age;

  private final String sex;

  private final String remark;

  private final boolean isDeleted;

  public Id getStudentId() {
    if (this.studentId == null) {
      throw new NullPointerException("studentIdがnullのため、getStudentId()を実行できません");
    }
    return studentId;
  }
}
