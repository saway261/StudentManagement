package raisetech.StudentManagement.data;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StudentCourse {

  private int courseId;
  private String courseName;
  private int studentId;
  private LocalDate courseStartAt;
  private LocalDate courseEndAt;

  public StudentCourse(String courseName, int studentId, LocalDate courseStartAt) {
    this.courseName = courseName;
    this.studentId = studentId;
    this.courseStartAt = courseStartAt;
    this.courseEndAt = courseStartAt.plusMonths(6);
  }

  public StudentCourse(int studentId) {
    this.courseName = "未登録";
    this.studentId = studentId;
    this.courseStartAt = LocalDate.now();
    this.courseEndAt = LocalDate.now().plusMonths(6);

  }
}
