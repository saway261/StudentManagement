package raisetech.student.management.web.form;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import raisetech.student.management.data.domain.validation.OnRegister;
import raisetech.student.management.data.domain.validation.OnUpdate;

@Data
@NoArgsConstructor
public class StudentDetailForm {

  @NotNull(groups = {OnRegister.class, OnUpdate.class})
  @Valid
  private StudentForm student;

  @NotEmpty(groups = {OnRegister.class, OnUpdate.class})
  @Valid
  private List<StudentCourseForm> studentCourseList;

}
