package raisetech.StudentManagement;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentCourse {

  private int courseId;
  private String courseName;
  private int studentId;
  private LocalDate courseStartAt;
  private LocalDate courseEndAt;

}
