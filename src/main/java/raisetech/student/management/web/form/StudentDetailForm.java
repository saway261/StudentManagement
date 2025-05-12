package raisetech.student.management.web.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import raisetech.student.management.data.domain.validation.OnRegister;
import raisetech.student.management.data.domain.validation.OnUpdate;

@Schema(description = "受講生詳細登録・更新用フォーム")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDetailForm {

  @NotNull(groups = {OnRegister.class, OnUpdate.class})
  @Valid
  private StudentForm student;

  @NotEmpty(groups = {OnRegister.class, OnUpdate.class})
  @Valid
  private List<StudentCourseForm> studentCourseList;

}
