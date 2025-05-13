package raisetech.student.management.web.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.domain.validation.OnRegister;
import raisetech.student.management.data.domain.validation.OnUpdate;
import raisetech.student.management.data.domain.validation.PhoneNumber;
import raisetech.student.management.data.value.Id;

@Schema(description = "受講生登録・更新用フォーム")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentForm {

  @Schema(description = "受講生ID 登録時は自動採番を行うため不要、更新時のみ必要", example = "1")
  @NotNull(groups = OnUpdate.class)
  @Positive(groups = OnUpdate.class)
  private Integer studentId;

  @Schema(description = "フルネーム", example = "佐藤花子")
  @NotNull(groups = {OnRegister.class, OnUpdate.class})
  @Size(max = 50, groups = {OnRegister.class, OnUpdate.class})
  private String fullname;

  @Schema(description = "かな", example = "さとうはなこ")
  @NotNull(groups = {OnRegister.class, OnUpdate.class})
  @Size(max = 50, groups = {OnRegister.class, OnUpdate.class})
  private String kanaName;

  @Schema(description = "ニックネーム", example = "ハナ")
  @Size(max = 50, groups = {OnRegister.class, OnUpdate.class})
  private String nickname;

  @Schema(description = "Emailアドレス", example = "hanako@example.com")
  @NotNull(groups = {OnRegister.class, OnUpdate.class})
  @Email(groups = {OnRegister.class, OnUpdate.class})
  @Size(max = 50, groups = {OnRegister.class, OnUpdate.class})
  private String email;

  @Schema(description = "居住地（市区町村まで）", example = "東京都品川区")
  @Size(max = 50, groups = {OnRegister.class, OnUpdate.class})
  private String area;

  @Schema(description = "電話番号", example = "090-1234-5678")
  @PhoneNumber(groups = {OnRegister.class, OnUpdate.class})
  private String telephone;

  @Schema(description = "年齢", example = "25")
  @Min(value = 15, groups = {OnRegister.class, OnUpdate.class})
  @Max(value = 80, groups = {OnRegister.class, OnUpdate.class})
  private Integer age;

  @Schema(description = "性別 '男''女''その他'のみが入力可能", example = "女")
  @Pattern(regexp = "^(男|女|その他)$", message = "性別は「男」「女」「その他」のいずれかを入力してください", groups = {
      OnRegister.class, OnUpdate.class})
  private String sex;

  @Schema(description = "備考", example = "転職活動中")
  @Size(max = 200, groups = {OnRegister.class, OnUpdate.class})
  private String remark;

  @Schema(description = "キャンセルフラグ", example = "false")
  private boolean isDeleted;

  Student toDomain(StudentForm form) { //package private
    return new Student(
        form.getStudentId() == null ? null : new Id(form.getStudentId()),
        form.getFullname(),
        form.getKanaName(),
        form.getNickname(),
        form.getEmail(),
        form.getArea(),
        form.getTelephone(),
        form.getAge(),
        form.getSex(),
        form.getRemark(),
        form.isDeleted()
    );
  }

}
