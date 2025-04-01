package raisetech.StudentManagement.data;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentCourse {

  private int courseId;
  private String courseName;
  private int studentId;
  private LocalDate courseStartAt;
  private LocalDate courseEndAt;

  public StudentCourse(String courseName, int studentId) {
    this.courseName = courseName;
    this.studentId = studentId;

    LocalDate now = LocalDate.now();
    this.courseStartAt = now;
    this.courseEndAt = now.plusMonths(6);
  }

  public StudentCourse(String courseName) {
    this.courseName = courseName;
    LocalDate now = LocalDate.now();
    this.courseStartAt = now;
    this.courseEndAt = now.plusMonths(6);
  }
}
