package reisetech.StudentManagement.data;

import lombok.Getter;

@Getter//rombokが勝手に作ってくれる
public class Student {

  private String id;
  private String fullname;
  private String furigana;
  private String nickname;
  private String email;
  private String city;
  private String telephone;
  private int age;
  private String gender;


}
