package raisetech.StudentManagement.data.domain;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;

//空のStudentDetailインスタンスは生成できない。
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentDetail {

  private Student student;
  private List<StudentCourse> studentCourseList;

  public StudentDetail(Student student) {
    this.student = student;
    this.studentCourseList.add(new StudentCourse("未登録", student.getStudentId()));
  }
}
