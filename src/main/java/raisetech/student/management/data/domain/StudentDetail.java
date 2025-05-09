package raisetech.student.management.data.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.domain.validation.OnRegister;
import raisetech.student.management.data.domain.validation.OnUpdate;

@Schema(description = "受講生詳細")
@Getter
@AllArgsConstructor
public class StudentDetail {

  @NotNull(groups = {OnRegister.class, OnUpdate.class})
  @Valid
  private final Student student;

  @NotEmpty(groups = {OnRegister.class, OnUpdate.class})
  @Valid
  private final List<StudentCourse> studentCourseList;

}
