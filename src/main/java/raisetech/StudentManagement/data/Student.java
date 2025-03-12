package raisetech.StudentManagement.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Student {

  private int studentId;
  private String fullname;
  private String kanaName;
  private String nickname;
  private String email;
  private String area;
  private String telephone;
  private int age;
  private String sex;
  private String remark;
  private boolean isDeleted;

}
