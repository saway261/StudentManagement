package raisetech.student.management.data.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;

//空のStudentDetailインスタンスは生成できない。
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentDetail {

  @NotNull
  @Valid
  private Student student;

  @NotEmpty
  @Valid
  private List<StudentCourse> studentCourseList;

  public StudentDetail(Student student) {
    this.student = student;
    this.studentCourseList.add(new StudentCourse("未登録", student.getStudentId()));
  }
}
