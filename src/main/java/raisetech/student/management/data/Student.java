package raisetech.student.management.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import raisetech.student.management.validation.CreateGroup;
import raisetech.student.management.validation.UpdateGroup;

@Schema(description = "受講生情報")
@Getter
@Setter
public class Student {

  @Schema(description = "受講生ID 登録時は自動採番を行うため不要、更新時のみ必要", example = "1")
  @NotNull(groups = UpdateGroup.class)
  @Positive(groups = UpdateGroup.class)
  private Integer studentId;

  @Schema(description = "フルネーム", example = "佐藤花子")
  @NotNull(groups = {CreateGroup.class, UpdateGroup.class})
  @Size(max = 50,groups = {CreateGroup.class, UpdateGroup.class})
  private String fullName;

  @Schema(description = "かな", example = "さとうはなこ")
  @NotNull(groups = {CreateGroup.class, UpdateGroup.class})
  @Size(max = 50,groups = {CreateGroup.class, UpdateGroup.class})
  private String kanaName;

  @Schema(description = "ニックネーム", example = "ハナ")
  @NotNull(groups = {CreateGroup.class, UpdateGroup.class})
  @Size(max = 50,groups = {CreateGroup.class, UpdateGroup.class})
  private String nickname;

  @Schema(description = "Emailアドレス", example = "hanako@example.com")
  @NotNull(groups = {CreateGroup.class, UpdateGroup.class})
  @Email(groups = {CreateGroup.class, UpdateGroup.class})
  private String email;

  @Schema(description = "居住地（市区町村まで）", example = "東京都品川区")
  @Size(max = 50,groups = {CreateGroup.class, UpdateGroup.class})
  private String area;

  @Schema(description = "電話番号", example = "090-1234-5678")
  @Pattern(
      regexp = "^(0[1-9]\\d{0,3}-\\d{1,4}-\\d{4}|0[789]0-\\d{4}-\\d{4})$",
      message = "電話番号は日本の固定電話または携帯電話をハイフン区切りで入力してください",
      groups = {CreateGroup.class, UpdateGroup.class}
  )
  private String telephone;

  @Schema(description = "年齢", example = "25")
  @Min(value = 15, groups = {CreateGroup.class, UpdateGroup.class})
  @Max(value = 80, groups = {CreateGroup.class, UpdateGroup.class})
  private Integer age;

  @Schema(description = "性別 '男''女''その他'のみが入力可能", example = "女")
  @Pattern(
      regexp = "^(男|女|その他)$",
      message = "性別は「男」「女」「その他」から選択できます。",
      groups = {CreateGroup.class, UpdateGroup.class}
  )
  private String sex;

  @Schema(description = "備考", example = "転職活動中")
  @Size(max = 200,groups = {CreateGroup.class, UpdateGroup.class})
  private String remark;

  @Schema(description = "削除フラグ", example = "false")
  private boolean isDeleted;
}
