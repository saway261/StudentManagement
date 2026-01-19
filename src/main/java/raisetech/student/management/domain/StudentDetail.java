package raisetech.student.management.domain;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;

@Getter
@Setter
@AllArgsConstructor
public class StudentDetail {

  private Student student;
  private List<StudentCourse> studentCourses;

}
