package raisetech.StudentManagement.data.domain;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;

//空のStudentDetailインスタンスは生成できない。
@Getter
@Setter
@AllArgsConstructor
public class StudentDetail {

  private Student student;
  private List<StudentCourse> studentsCourses;

  public StudentDetail(Student student) {
    this.student = student;
    this.studentsCourses.add(new StudentCourse(student.getStudentId()));
  }
}
