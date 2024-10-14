package reisetech.StudentManagement.data;

import lombok.Getter;

@Getter//lombokが勝手に作ってくれる
public class Student {

  private String studentId;
  private String fullname;
  private String furigana;
  private String nickname;
  private String email;
  private String city;
  private String telephone;
  private int age;
  private String gender;
  private String remark;
  private boolean isDeleted;
  //上2項目をDBに追加する


}
