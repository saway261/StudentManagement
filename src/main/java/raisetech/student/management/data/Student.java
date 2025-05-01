package raisetech.student.management.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import raisetech.student.management.data.domain.validation.OnUpdate;
import raisetech.student.management.data.domain.validation.PhoneNumber;

@Schema(description = "受講生")
@Getter
public class Student {

  @Schema(description = "受講生ID 自動採番を行う", example = "1")
  @Positive(groups = OnUpdate.class)
  private final int studentId;

  @Schema(description = "フルネーム", example = "佐藤花子")
  @NotNull
  @Length(max = 50)
  private final String fullname;

  @Schema(description = "かな", example = "さとうはなこ")
  @NotNull
  @Length(max = 50)
  private final String kanaName;

  @Schema(description = "ニックネーム", example = "ハナ")
  @Length(max = 50)
  private final String nickname;

  @Schema(description = "Emailアドレス", example = "hanako@raisetech.com")
  @NotNull
  @Email
  @Length(max = 50)
  private final String email;

  @Schema(description = "居住地（都道府県）", example = "東京都")
  @Length(max = 50)
  private final String area;

  @Schema(description = "電話番号", example = "070-0000-0000")
  @PhoneNumber
  private final String telephone;

  @Schema(description = "年齢", example = "20")
  @Min(15)
  @Max(80)
  private final int age;

  @Schema(description = "性別 '男''女''その他'のみが入力可能", example = "女")
  @Pattern(regexp = "^(男|女|その他)$", message = "性別は「男」「女」「その他」のいずれかを入力してください")
  private final String sex;

  @Schema(description = "備考", example = "転職活動中")
  @Length(max = 200)
  private final String remark;

  @Schema(description = "キャンセルフラグ", example = "false")
  private final boolean isDeleted;

  @JsonCreator
  public Student(@JsonProperty("studentId") int studentId,
      @JsonProperty("fullname") String fullname,
      @JsonProperty("kanaName") String kanaName,
      @JsonProperty("nickname") String nickname,
      @JsonProperty("email") String email,
      @JsonProperty("area") String area,
      @JsonProperty("telephone") String telephone,
      @JsonProperty("age") int age,
      @JsonProperty("sex") String sex,
      @JsonProperty("remark") String remark,
      @JsonProperty("isDeleted") boolean isDeleted) {
    this.studentId = studentId;
    this.fullname = fullname;
    this.kanaName = kanaName;
    this.nickname = nickname;
    this.email = email;
    this.area = area;
    this.telephone = telephone;
    this.age = age;
    this.sex = sex;
    this.remark = remark;
    this.isDeleted = isDeleted;
  }

}
