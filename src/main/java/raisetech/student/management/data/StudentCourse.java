package raisetech.student.management.data;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentCourse {

  private int courseId;
  private int studentId;
  private String courseName;
  private LocalDate courseStartAt;
  private LocalDate courseEndAt;

}
