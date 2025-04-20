package raisetech.student.management.data.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;

@Schema(description = "受講生詳細")
@Getter
@Setter
@AllArgsConstructor
public class StudentDetail {

  @NotNull
  @Valid
  private Student student;

  @NotEmpty
  @Valid
  private List<StudentCourse> studentCourseList;

}
