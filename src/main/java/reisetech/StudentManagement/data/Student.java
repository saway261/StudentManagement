package reisetech.StudentManagement.data;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter//lombokが勝手に作ってくれる
public class Student {

  private String studentId;
  private String fullname;
  private String kanaName;
  private String nickname;
  private String email;
  private String city;
  private String telephone;
  private int age;
  private String gender;
  private String remark;
  private int isDeleted;

}
