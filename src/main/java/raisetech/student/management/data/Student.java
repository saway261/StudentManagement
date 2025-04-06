package raisetech.student.management.data;

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

@Getter
@Setter
public class Student {

  @Positive(groups = OnUpdate.class)
  private int studentId;

  @NotNull
  @Length(max = 50)
  private String fullname;

  @NotNull
  @Length(max = 50)
  private String kanaName;

  @Length(max = 50)
  private String nickname;

  @Email
  @Length(max = 50)
  private String email;

  @Length(max = 50)
  private String area;

  @PhoneNumber
  private String telephone;

  @Positive
  @Min(15)
  @Max(80)
  private int age;

  @Pattern(regexp = "^(男|女|その他)$", message = "性別は「男」「女」「その他」のいずれかを入力してください")
  private String sex;

  @Length(max = 200)
  private String remark;

  private boolean isDeleted;

}
