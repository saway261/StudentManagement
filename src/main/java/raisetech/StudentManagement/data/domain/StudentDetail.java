package raisetech.StudentManagement.data.domain;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;

@Getter
@Setter
@NoArgsConstructor
public class StudentDetail {

  private Student student;
  private List<StudentCourse> studentsCourses;

}
