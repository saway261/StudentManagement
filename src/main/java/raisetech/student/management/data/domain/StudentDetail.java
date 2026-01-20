package raisetech.student.management.data.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.validation.CreateGroup;
import raisetech.student.management.validation.UpdateGroup;

@Getter
@Setter
@AllArgsConstructor
public class StudentDetail {

  @NotNull(groups = {CreateGroup.class, UpdateGroup.class})
  @Valid
  private Student student;

  @NotEmpty(groups = {CreateGroup.class, UpdateGroup.class})
  @Valid
  private List<StudentCourse> studentCourses;

}
