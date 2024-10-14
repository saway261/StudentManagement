package reisetech.StudentManagement.data;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StudentsCourses {

  private String courseId;
  private String course;
  private String studentId;
  private LocalDate courseStartAt;
  private LocalDate courseEndAt;
}
