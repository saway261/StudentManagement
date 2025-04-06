package raisetech.student.management.data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raisetech.student.management.data.domain.validation.CourseName;
import raisetech.student.management.data.domain.validation.OnUpdate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentCourse {

  @Positive(groups = OnUpdate.class)
  private int courseId;

  @NotNull
  @CourseName
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
}
