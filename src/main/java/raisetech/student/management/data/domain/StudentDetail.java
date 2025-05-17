package raisetech.student.management.data.domain;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;

@Getter
@AllArgsConstructor
public class StudentDetail {

  private final Student student;

  private final List<StudentCourse> studentCourseList;

}
