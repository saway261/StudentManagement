package reisetech.StudentManagement.data;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StudentsCourses {

  private int courseId;
  private String courseName;
  private int studentId;
  private LocalDate courseStartAt;
  private LocalDate courseEndAt;
}
