package raisetech.student.management.data;

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

@Getter
@Setter
public class Student {

  @NotNull(groups = UpdateGroup.class)
  @Positive(groups = UpdateGroup.class)
  private Integer studentId;

  @NotNull(groups = {CreateGroup.class, UpdateGroup.class})
  @Size(max = 50,groups = {CreateGroup.class, UpdateGroup.class})
  private String fullName;

  @NotNull(groups = {CreateGroup.class, UpdateGroup.class})
  @Size(max = 50,groups = {CreateGroup.class, UpdateGroup.class})
  private String kanaName;

  @NotNull(groups = {CreateGroup.class, UpdateGroup.class})
  @Size(max = 50,groups = {CreateGroup.class, UpdateGroup.class})
  private String nickname;

  @NotNull(groups = {CreateGroup.class, UpdateGroup.class})
  @Email(groups = {CreateGroup.class, UpdateGroup.class})
  private String email;

  @Size(max = 50,groups = {CreateGroup.class, UpdateGroup.class})
  private String area;

  @Pattern(
      regexp = "^(0[1-9]\\d{0,3}-\\d{1,4}-\\d{4}|0[789]0-\\d{4}-\\d{4})$",
      message = "電話番号は日本の固定電話または携帯電話をハイフン区切りで入力してください",
      groups = {CreateGroup.class, UpdateGroup.class}
  )
  private String telephone;

  @Min(value = 15, groups = {CreateGroup.class, UpdateGroup.class})
  @Max(value = 80, groups = {CreateGroup.class, UpdateGroup.class})
  private Integer age;

  @Pattern(
      regexp = "^(男|女|その他)$",
      message = "性別は「男」「女」「その他」から選択できます。",
      groups = {CreateGroup.class, UpdateGroup.class}
  )
  private String sex;

  @Size(max = 200,groups = {CreateGroup.class, UpdateGroup.class})
  private String remark;

  private boolean isDeleted;
}
