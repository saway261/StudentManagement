package raisetech.student.management.web.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import raisetech.student.management.data.Student;

@Schema(description = "受講生情報（出力用）")
@Getter
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

  @Schema(description = "居住地（市区町村まで）", example = "東京都品川区")
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

  public StudentResponse(Student domain) {
    this.studentId = domain.getStudentId().getValue(); // Id → int
    this.fullname = domain.getFullname();
    this.kanaName = domain.getKanaName();
    this.nickname = domain.getNickname();
    this.email = domain.getEmail();
    this.area = domain.getArea();
    this.telephone = domain.getTelephone();
    this.age = domain.getAge();
    this.sex = domain.getSex();
    this.remark = domain.getRemark();
    this.isDeleted = domain.isDeleted();
  }
}

