package raisetech.student.management.web.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import raisetech.student.management.data.domain.validation.OnRegister;
import raisetech.student.management.data.domain.validation.OnUpdate;
import raisetech.student.management.data.domain.validation.PhoneNumber;

@Data
@NoArgsConstructor
public class StudentForm {

  @NotNull(groups = OnUpdate.class)
  @Positive(groups = OnUpdate.class)
  private Integer studentId;

  @NotNull(groups = {OnRegister.class, OnUpdate.class})
  @Size(max = 50, groups = {OnRegister.class, OnUpdate.class})
  private String fullname;

  @NotNull(groups = {OnRegister.class, OnUpdate.class})
  @Size(max = 50, groups = {OnRegister.class, OnUpdate.class})
  private String kanaName;

  @Size(max = 50, groups = {OnRegister.class, OnUpdate.class})
  private String nickname;

  @NotNull(groups = {OnRegister.class, OnUpdate.class})
  @Email(groups = {OnRegister.class, OnUpdate.class})
  @Size(max = 50, groups = {OnRegister.class, OnUpdate.class})
  private String email;

  @Size(max = 50, groups = {OnRegister.class, OnUpdate.class})
  private String area;

  @PhoneNumber(groups = {OnRegister.class, OnUpdate.class})
  private String telephone;

  @Min(value = 15, groups = {OnRegister.class, OnUpdate.class})
  @Max(value = 80, groups = {OnRegister.class, OnUpdate.class})
  private Integer age;

  @Pattern(regexp = "^(男|女|その他)$", message = "性別は「男」「女」「その他」のいずれかを入力してください", groups = {
      OnRegister.class, OnUpdate.class})
  private String sex;

  @Size(max = 200, groups = {OnRegister.class, OnUpdate.class})
  private String remark;

  private boolean isDeleted;

}
