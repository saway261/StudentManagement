package raisetech.student.management.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import raisetech.student.management.data.domain.validation.OnUpdate;
import raisetech.student.management.data.domain.validation.PhoneNumber;

@Schema(description = "受講生")
@Getter
@Setter
public class Student {

  @Schema(description = "受講生ID 自動採番を行う", example = "1")
  @Positive(groups = OnUpdate.class)
  private int studentId;

  @Schema(description = "フルネーム", example = "佐藤花子")
  @NotNull
  @Length(max = 50)
  private String fullname;

  @Schema(description = "かな", example = "さとうはなこ")
  @NotNull
  @Length(max = 50)
  private String kanaName;

  @Schema(description = "ニックネーム", example = "ハナ")
  @Length(max = 50)
  private String nickname;

  @Schema(description = "Emailアドレス", example = "hanako@raisetech.com")
  @Email
  @Length(max = 50)
  private String email;

  @Schema(description = "居住地（都道府県）", example = "東京都")
  @Length(max = 50)
  private String area;

  @Schema(description = "電話番号", example = "070-0000-0000")
  @PhoneNumber
  private String telephone;

  @Schema(description = "年齢", example = "20")
  @Positive
  @Min(15)
  @Max(80)
  private int age;

  @Schema(description = "性別 '男''女''その他'のみが入力可能", example = "女")
  @Pattern(regexp = "^(男|女|その他)$", message = "性別は「男」「女」「その他」のいずれかを入力してください")
  private String sex;

  @Schema(description = "備考", example = "転職活動中")
  @Length(max = 200)
  private String remark;

  @Schema(description = "キャンセルフラグ", example = "false")
  private boolean isDeleted;

}
