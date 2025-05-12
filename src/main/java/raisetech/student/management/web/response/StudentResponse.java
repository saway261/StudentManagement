package raisetech.student.management.web.response;

import io.swagger.v3.oas.annotations.media.Schema;
import raisetech.student.management.data.Student;

@Schema(description = "受講生情報（レスポンス）")
public class StudentResponse {

  @Schema(description = "受講生ID", example = "1")
  private final Integer studentId;

  @Schema(description = "フルネーム", example = "佐藤花子")
  private final String fullname;

  @Schema(description = "かな", example = "さとうはなこ")
  private final String kanaName;

  @Schema(description = "ニックネーム", example = "ハナ")
  private final String nickname;

  @Schema(description = "メールアドレス", example = "hana@example.com")
  private final String email;

  @Schema(description = "住所", example = "東京都品川区")
  private final String area;

  @Schema(description = "電話番号", example = "090-1234-5678")
  private final String telephone;

  @Schema(description = "年齢", example = "25")
  private final Integer age;

  @Schema(description = "性別", example = "女")
  private final String sex;

  @Schema(description = "備考", example = "転職活動中")
  private final String remark;

  @Schema(description = "削除フラグ", example = "false")
  private final boolean isDeleted;

  public StudentResponse(Student student) {
    this.studentId = student.getStudentId().getValue(); // Id → int
    this.fullname = student.getFullname();
    this.kanaName = student.getKanaName();
    this.nickname = student.getNickname();
    this.email = student.getEmail();
    this.area = student.getArea();
    this.telephone = student.getTelephone();
    this.age = student.getAge();
    this.sex = student.getSex();
    this.remark = student.getRemark();
    this.isDeleted = student.isDeleted();
  }

  // getter（必要に応じて lombok @Getter を使ってもOK）
}

